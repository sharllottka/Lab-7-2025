package threads;

import static functions.Functions.integrate;

public class SimpleIntegrator implements Runnable {
    private Task task;

    public SimpleIntegrator (Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTaskCount(); i++) {
            /*if (task.getFunction() == null) {
                continue;
            }*/
            synchronized (task) {
                while (!task.isReady()) {
                    try {
                        task.wait();
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                double integrated = integrate(task.getFunction(), task.getLeftDomainBorder(), task.getRightDomainBorder(), task.getStep());
                System.out.println("Result: " + task.getLeftDomainBorder() + " " + task.getRightDomainBorder() + " " + task.getStep() + " " + integrated);
                task.setReady(false);
                task.notify();
            }
        }
    }
}
