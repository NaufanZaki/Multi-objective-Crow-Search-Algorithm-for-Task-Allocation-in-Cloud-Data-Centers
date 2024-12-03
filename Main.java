import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create hosts and VMs based on the specifications
        Host host1 = new Host(1, 4096, 1860);
        host1.addVM(new VM(0, 512, 250));
        host1.addVM(new VM(1, 1024, 500));

        Host host2 = new Host(2, 4096, 2660);
        host2.addVM(new VM(2, 1024, 1000));
        host2.addVM(new VM(3, 2048, 1000));

        List<Task> tasks50 = generateTasks(50);
        List<Task> tasks100 = generateTasks(100);
        List<Task> tasks150 = generateTasks(150);
        List<Task> tasks200 = generateTasks(200);

        // Create CSA instance with tasks for 50 tasks
        CSA csa50 = new CSA(tasks50, Arrays.asList(host1, host2));

        // Create scheduler and simulation instances for each task count
        TaskScheduler scheduler50 = new TaskScheduler(tasks50, Arrays.asList(host1, host2));
        Simulation simulation = new Simulation();

        System.out.println("Running simulation for 50 tasks...");
        simulation.runSimulation(tasks50, scheduler50, csa50);

        // Repeat the process for 100, 150, 200 tasks
        CSA csa100 = new CSA(tasks100, Arrays.asList(host1, host2));
        TaskScheduler scheduler100 = new TaskScheduler(tasks100, Arrays.asList(host1, host2));
        System.out.println("Running simulation for 100 tasks...");
        simulation.runSimulation(tasks100, scheduler100, csa100);
        
        CSA csa150 = new CSA(tasks150, Arrays.asList(host1, host2));
        TaskScheduler scheduler150 = new TaskScheduler(tasks150, Arrays.asList(host1, host2));
        System.out.println("Running simulation for 150 tasks...");
        simulation.runSimulation(tasks150, scheduler150, csa150);
        
        CSA csa200 = new CSA(tasks200, Arrays.asList(host1, host2));
        TaskScheduler scheduler200 = new TaskScheduler(tasks200, Arrays.asList(host1, host2));
        System.out.println("Running simulation for 200 tasks...");
        simulation.runSimulation(tasks200, scheduler200, csa200);
    }

    // Utility function to generate tasks with random CPU and RAM requirements within a reasonable range
    private static List<Task> generateTasks(int numTasks) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            // Generate tasks with CPU and RAM requirements within reasonable limits (based on VM capacities)
            int cpuRequired = (int) (Math.random() * 1000) + 100; // CPU between 100 and 1000 MIPS
            int ramRequired = (int) (Math.random() * 500) + 100; // RAM between 100 and 500 MB
            tasks.add(new Task(i, cpuRequired, ramRequired));
        }
        return tasks;
    }
}