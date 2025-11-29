package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class TabulatedFunctions {
    private static TabulatedFunctionFactory functionFactory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();
    private TabulatedFunctions() {}

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }
        double step = (rightX - leftX) / (pointsCount - 1);
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        int pointsCount = function.getPointsCount();
        dataOut.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointsCount = dataIn.readInt();
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        out.write(String.valueOf(function.getPointsCount()));
        out.write(" ");
        for (int i = 0; i < function.getPointsCount(); i++) {
            out.write(String.valueOf(function.getPointX(i)));
            out.write(" ");
            out.write(String.valueOf(function.getPointY(i)));
            out.write(" ");
        }
        out.write("\n");
        out.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException{
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int pointsCount = (int) st.nval;
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }
        for (int i = 0; i < pointsCount; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(points);
    }

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory functionFactory) {
        TabulatedFunctions.functionFactory = functionFactory;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return functionFactory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return functionFactory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return functionFactory.createTabulatedFunction(points);
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, double leftX, double rightX, double[] values) {
        if (clazz == null || values == null) {
            throw new IllegalArgumentException("Пустой параметр");
        }
        try {
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, double leftX, double rightX, int pointsCount) {
        if (clazz == null) {
            throw new IllegalArgumentException("Пустой параметр");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }
        try {
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, FunctionPoint[] points) {
        if (clazz == null || points == null) {
            throw new IllegalArgumentException("Пустой параметр");
        }
        try {
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) points);
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> clazz, Function function, double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }
        double step = (rightX - leftX) / (pointsCount - 1);
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(clazz, points);
    }

    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> clazz, InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointsCount = dataIn.readInt();
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(clazz, points);
    }

    public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> clazz, Reader in) throws IOException{
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int pointsCount = (int) st.nval;
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек < 2");
        }
        for (int i = 0; i < pointsCount; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(clazz, points);
    }
}