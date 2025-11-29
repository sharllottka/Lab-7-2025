package threads;

import functions.basic.Log;

import java.util.concurrent.Semaphore;

import static functions.Functions.integrate;

public class Integrator extends Thread {
    private Task task;
    private final Semaphore writeSem;
    private final Semaphore readSem;

    public Integrator(Task task, Semaphore writeSem, Semaphore readSem) {
        this.task = task;
        this.writeSem = writeSem;
        this.readSem = readSem;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                readSem.acquire();
                double integrated = integrate(task.getFunction(), task.getLeftDomainBorder(), task.getRightDomainBorder(), task.getStep());
                System.out.println("Result: " + task.getLeftDomainBorder() + " " + task.getRightDomainBorder() + " " + task.getStep() + " " + integrated);
            }
            catch (InterruptedException e) {
                System.out.println("Integrator прерван");
                return;
            }
            finally {
                writeSem.release();
            }
            Thread.yield();
        }
    }
}
