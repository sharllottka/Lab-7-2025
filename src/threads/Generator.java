package threads;

import functions.basic.Log;

import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    private Task task;
    private final Semaphore writeSem;
    private final Semaphore readSem;

    public Generator(Task task, Semaphore writeSem, Semaphore readSem) {
        this.task = task;
        this.writeSem = writeSem;
        this.readSem = readSem;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                writeSem.acquire();
                double base = 1 + Math.random() * 9;
                task.setFunction(new Log(base));
                double right = 100 + Math.random() * 100;
                double left = Math.random() * 100;
                task.setDomainBorder(left, right);
                task.setStep(0.1 + Math.random() * 0.9);
                System.out.println("Source: " + task.getLeftDomainBorder() + " " + task.getRightDomainBorder() + " " + task.getStep());
            }
            catch (InterruptedException e) {
                System.out.println("Generator прерван");
                return;
            }
            finally{
                readSem.release();
            }
            Thread.yield();
        }
    }
}
