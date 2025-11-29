import functions.*;
import functions.basic.*;
import threads.*;

import static functions.Functions.integrate;
import java.io.*;
import java.util.concurrent.Semaphore;

public class Main {

    /*public void nonThread() {
        Task task = new Task(null, 0, 0, 0, 100);
        for (int i = 0; i < task.getTaskCount(); i++) {
            double base = 1 + Math.random() * 9;
            task.setFunction(new Log(base));
            task.setRightDomainBorder(100 + Math.random() * 100);
            task.setLeftDomainBorder(Math.random() * 100);
            task.setStep(0.1 + Math.random() * 0.9);
            System.out.println("Source: " + task.getLeftDomainBorder() + " " + task.getRightDomainBorder() + " " + task.getStep());
            double integrated = integrate(task.getFunction(), task.getLeftDomainBorder(), task.getRightDomainBorder(), task.getStep());
            System.out.println("Result: " + task.getLeftDomainBorder() + " " + task.getRightDomainBorder() + " " + task.getStep() + " " + integrated);
        }
    }

    public void simpleThread() throws InterruptedException {
        Task task = new Task(null, 0, 0, 0, 100);
        SimpleGenerator generator = new SimpleGenerator(task);
        SimpleIntegrator integrator = new SimpleIntegrator(task);
        Thread generatorThread = new Thread(generator);
        Thread integratorThread = new Thread(integrator);
        generatorThread.setPriority(Thread.MAX_PRIORITY);
        integratorThread.setPriority(Thread.MIN_PRIORITY);
        generatorThread.start();
        integratorThread.start();
        generatorThread.join();
        integratorThread.join();
    }

    public void complicatedThreads() throws InterruptedException {
        Task task = new Task(null, 0, 0, 0, 100);
        Semaphore writeSem = new Semaphore(1);
        Semaphore readSem = new Semaphore(0);
        Generator generator = new Generator(task, writeSem, readSem);
        Integrator integrator = new Integrator(task, writeSem, readSem);
        generator.setPriority(Thread.MAX_PRIORITY);
        integrator.setPriority(Thread.MIN_PRIORITY);
        generator.start();
        integrator.start();
        Thread.sleep(50);
        generator.interrupt();
        integrator.interrupt();
    }*/

    public static void main(String[] args) throws IOException, InterruptedException {
        double EPS = 1e-9;

        System.out.println("Проверка итератора ArrayTabulatedFunction:");
        TabulatedFunction func1 = new ArrayTabulatedFunction(new FunctionPoint[]{new FunctionPoint(0,0), new FunctionPoint(1,1), new FunctionPoint(2,4)});
        for (FunctionPoint p : func1) {
            System.out.println(p);
        }
        System.out.println();

        System.out.println("Проверка итератора LinkedListTabulatedFunction:");
        TabulatedFunction func2 = new LinkedListTabulatedFunction(new FunctionPoint[]{new FunctionPoint(0,0), new FunctionPoint(1,1), new FunctionPoint(2,4)});
        for (FunctionPoint p : func2) {
            System.out.println(p);
        }
        System.out.println();

        System.out.println("Проверка работы Фабрики:");
        Function sin = new Sin();
        TabulatedFunction tabSin;
        tabSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 11);
        System.out.println(tabSin.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tabSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 11);
        System.out.println(tabSin.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tabSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 11);
        System.out.println(tabSin.getClass());
        System.out.println();

        System.out.println("Проверка работы методов рефлексивного создания объектов:");
        TabulatedFunction func;
        func = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(func.getClass());
        System.out.println(func);
        func = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(func.getClass());
        System.out.println(func);
        func = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class, new FunctionPoint[] {new FunctionPoint(0, 0), new FunctionPoint(10, 10)});
        System.out.println(func.getClass());
        System.out.println(func);
        func = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Cos(), 0, Math.PI, 11);
        System.out.println(func.getClass());
        System.out.println(func);
    }
}
