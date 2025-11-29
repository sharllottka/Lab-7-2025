package functions;

import org.w3c.dom.Node;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

// табулированная функция на двусвязном списке
//public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable {
public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {

    private class FunctionNode {
        FunctionPoint point;
        FunctionNode next;
        FunctionNode prev;

        FunctionNode(FunctionPoint p) {
            this.point = new FunctionPoint(p);
        }
    }

    private FunctionNode head; // фиктивная голова списка
    private int pointsCount;
    private static final double EPS = 1e-9;
    private FunctionPoint[] points;

    public LinkedListTabulatedFunction() {
        head = new FunctionNode(new FunctionPoint());
        head.next = head;
        head.prev = head;
        pointsCount = 0;
    }

    public LinkedListTabulatedFunction(TabulatedFunction func) {
        this(toPointsArray(func));
    }

    public static FunctionPoint[] toPointsArray(TabulatedFunction func) {
        FunctionPoint[] funcPoints = new FunctionPoint[func.getPointsCount()];
        for (int i = 0; i < func.getPointsCount(); i++) {
            funcPoints[i] = func.getPoint(i);
        }
        return funcPoints;
    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }
        Arrays.sort(points, Comparator.comparingDouble(FunctionPoint::getX));
        for (int i = 0; i < points.length - 1; i++) {
            if (Math.abs(points[i + 1].getX() - points[i].getX()) <= EPS) {
                throw new IllegalArgumentException("Такая точка уже есть");
            }
        }

        head = new FunctionNode(new FunctionPoint());
        head.next = head;
        head.prev = head;
        pointsCount = 0;
        for (FunctionPoint p : points) {
            addNodeToTail(p);
        }

        this.points = new FunctionPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
        this.pointsCount = points.length;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }

        head = new FunctionNode(new FunctionPoint());
        head.next = head;
        head.prev = head;

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            addNodeToTail(new FunctionPoint(leftX + i * step, values[i]));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }

        head = new FunctionNode(new FunctionPoint());
        head.next = head;
        head.prev = head;

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = 0;
            addNodeToTail(new FunctionPoint(x, y));
        }
    }

    private void addNodeToTail(FunctionPoint p) {
        FunctionNode node = new FunctionNode(p);
        if (pointsCount == 0) {
            head.next = node;
            head.prev = node;
            node.next = head;
            node.prev = head;
        } else {
            FunctionNode last = head.prev;
            last.next = node;
            node.prev = last;
            node.next = head;
            head.prev = node;
        }
        pointsCount++;
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс: " + index);
        FunctionNode node = head.next;
        for (int i = 0; i < index; i++) node = node.next;
        return node;
    }

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }
    }

    @Override
    public int getPointsCount() {
        return pointsCount;
    }

    @Override
    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException();
        if ((index > 0 && point.getX() <= getPointX(index - 1) + EPS) ||
                (index < pointsCount - 1 && point.getX() >= getPointX(index + 1) - EPS))
            throw new InappropriateFunctionPointException("x нарушает порядок точек");

        getNodeByIndex(index).point = new FunctionPoint(point);
    }

    @Override
    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        setPoint(index, new FunctionPoint(x, getPointY(index)));
    }

    @Override
    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }

    @Override
    public void setPointY(int index, double y) {
        getNodeByIndex(index).point.setY(y);
    }

    @Override
    public void deletePoint(int index) {
        if (pointsCount <= 2)
            throw new IllegalStateException("Нельзя удалить — останется меньше двух точек");

        FunctionNode node = getNodeByIndex(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        pointsCount--;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode cur = head.next;
        while (cur != head) {
            if (Math.abs(cur.point.getX() - point.getX()) < EPS)
                throw new InappropriateFunctionPointException("Такая точка уже есть");
            if (cur.point.getX() > point.getX() + EPS)
                break;
            cur = cur.next;
        }
        FunctionNode node = new FunctionNode(point);
        node.next = cur;
        node.prev = cur.prev;
        cur.prev.next = node;
        cur.prev = node;
        pointsCount++;
    }

    @Override
    public double getLeftDomainBorder() {
        return head.next.point.getX();
    }

    @Override
    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;
        FunctionNode cur = head.next;
        while (cur.next != head) {
            if (Math.abs(cur.point.getX() - x) < EPS) {
                return cur.point.getY();
            }
            double x1 = cur.point.getX();
            double x2 = cur.next.point.getX();
            if (x >= x1 && x <= x2) {
                double y1 = cur.point.getY();
                double y2 = cur.next.point.getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            cur = cur.next;
        }
        return Double.NaN;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(current.point.getX());
            out.writeDouble(current.point.getY());
            current = current.next;
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        head = new FunctionNode(new FunctionPoint());
        head.next = head;
        head.prev = head;
        pointsCount = 0;
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            addNodeToTail(new FunctionPoint(x, y));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        FunctionNode current = head.next;
        while (current != head) {
                sb.append("(")
                        .append(current.point.getX()).append("; ")
                        .append(current.point.getY()).append(")");
                if (current != head.prev) {
                    sb.append(", ");
                }
                current = current.next;
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TabulatedFunction)) {
            return false;
        }
        TabulatedFunction other = (TabulatedFunction) o;
        if (this.getPointsCount() != other.getPointsCount()) {
            return false;
        }
        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction otherList = (LinkedListTabulatedFunction) o;
            FunctionNode current1 = this.head.next;
            FunctionNode current2 = otherList.head.next;
            while (current1 != head && current2 != head) {
                if (!current1.point.equals(current2.point)) {
                    return false;
                }
                current1 = current1.next;
                current2 = current2.next;
            }
        }
        else {
            for (int i = 0; i < pointsCount; i++) {
                if (!this.getPoint(i).equals(other.getPoint(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = pointsCount;
        FunctionNode current = head.next;
        while (current != head) {
            hash ^= current.point.hashCode();
            current = current.next;
        }
        return hash;
    }

    @Override
    public Object clone() {
        FunctionPoint[] funcPoints = new FunctionPoint[pointsCount];
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            funcPoints[i] = new FunctionPoint(current.point);
            current = current.next;
        }
        TabulatedFunction clone = TabulatedFunctions.createTabulatedFunction(funcPoints);
        return clone;
    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode current = LinkedListTabulatedFunction.this.head.next;
            @Override
            public boolean hasNext() {
                return current != LinkedListTabulatedFunction.this.head;
            }

            @Override
            public FunctionPoint next() {
                if (current == LinkedListTabulatedFunction.this.head) {
                    throw new NoSuchElementException();
                }
                FunctionPoint fp = new FunctionPoint(current.point);
                current = current.next;
                return fp;
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}