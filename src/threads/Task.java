package threads;

import functions.Function;

public class Task {
    private volatile boolean ready = false;
    Function f;
    double leftDomainBorder;
    double rightDomainBorder;
    double step;
    int taskCount;

    public synchronized boolean isReady() {
        return ready;
    }

    public synchronized void setReady(boolean ready) {
        this.ready = ready;
    }

    public Task(Function f, double leftDomainBorder, double rightDomainBorder, double step, int taskCount) {
        this.f = f;
        this.leftDomainBorder = leftDomainBorder;
        this.rightDomainBorder = rightDomainBorder;
        this.step = step;
        this.taskCount = taskCount;
    }

    public Function getFunction() {
        return f;
    }

    public double getLeftDomainBorder() {
        return leftDomainBorder;
    }

    public double getRightDomainBorder() {
        return rightDomainBorder;
    }

    public double getStep() {
        return step;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setFunction(Function f) {
        this.f = f;
    }

    public void setLeftDomainBorder(double leftDomainBorder) {
        if (leftDomainBorder >= rightDomainBorder) {
            throw new IllegalStateException("Левая граница >= правой");
        }
        this.leftDomainBorder = leftDomainBorder;
    }

    public void setRightDomainBorder(double rightDomainBorder) {
        if (leftDomainBorder >= rightDomainBorder) {
            throw new IllegalStateException("Правая граница <= левой");
        }
        this.rightDomainBorder = rightDomainBorder;
    }

    public void setStep(double step) {
        if (step < 0) {
            throw new IllegalStateException("Недопустимый шаг");
        }
        this.step = step;
    }

    public void setTaskCount(int taskCount) {
        if (taskCount < 0) {
            throw new IllegalStateException("Недопустимое количество заданий");
        }
        this.taskCount = taskCount;
    }

    public void setDomainBorder(double leftDomainBorder, double rightDomainBorder) {
        if (leftDomainBorder >= rightDomainBorder) {
            throw new IllegalStateException("Левая граница >= правой");
        }
        this.leftDomainBorder = leftDomainBorder;
        this.rightDomainBorder = rightDomainBorder;
    }
}
