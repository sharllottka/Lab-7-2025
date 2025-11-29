package threads;

import functions.basic.*;

public class SimpleGenerator implements Runnable {
    private Task task;

    public SimpleGenerator (Task task) {
        this.task = task;
    }

    public void run(){
        for (int i = 0; i < task.getTaskCount(); i++) {
            synchronized (task) {
                while (task.isReady()) {
                    try {
                        task.wait();
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                double base = 1 + Math.random() * 9;
                task.setFunction(new Log(base));
                double right = 100 + Math.random() * 100;
                double left = Math.random() * 100;
                task.setDomainBorder(left, right);
                task.setStep(0.1 + Math.random() * 0.9);
                System.out.println("Source: " + task.getLeftDomainBorder() + " " + task.getRightDomainBorder() + " " + task.getStep());
                task.setReady(true);
                task.notify();
            }
        }
    }
}
