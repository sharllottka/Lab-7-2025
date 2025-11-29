package functions;

import functions.meta.*;

public class Functions {

    private Functions() { }

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    public static double integrate(Function f, double leftDomainBorder, double rightDomainBorder, double step) {
        if (step <= 0 || leftDomainBorder < f.getLeftDomainBorder() || rightDomainBorder > f.getRightDomainBorder()) {
            throw new IllegalStateException("Недопустимый шаг или интервал интегрирования выходит за границы области определения функции");
        }
        double sSum = 0;
        for (double i = leftDomainBorder; i <= rightDomainBorder; i += step) {
            double x1 = i;
            double x2 = Math.min(i + step, rightDomainBorder);
            double a = f.getFunctionValue(x1);
            double b = f.getFunctionValue(x2);
            double s = (a + b) / 2 * (x2 - x1);
            sSum += s;
        }
        return sSum;
    }
}