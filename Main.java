import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create hosts based on HP ProLiant specifications from the paper
        Host host1 = new Host(1, 1860, 4096); // HP ProLiant ML110 G4
        Host host2 = new Host(2, 2660, 4096); // HP ProLiant ML110 G5

        // Create VMs based on Amazon EC2 instance types mentioned in the paper
        host1.addVM(new VM(0, 250, 512));  // Small instance
        host1.addVM(new VM(1, 500, 1024)); // Medium instance
        host2.addVM(new VM(2, 1000, 1024)); // Large instance
        host2.addVM(new VM(3, 1000, 2048)); // Extra Large instance
        host2.addVM(new VM(4, 2000, 2048)); // Double Extra Large instance

        // Generate task sets
        System.out.println("Generating tasks...");
        List<Task> tasks50 = generateTasks(50);
        List<Task> tasks100 = generateTasks(100);
        List<Task> tasks150 = generateTasks(150);
        List<Task> tasks200 = generateTasks(200);

        // Run simulation for 50 tasks
        System.out.println("\nRunning simulation for 50 tasks...");
        runSimulationSet(tasks50, host1, host2);

        // Reset host resources
        resetHosts(host1, host2);

        // Run simulation for 100 tasks
        System.out.println("\nRunning simulation for 100 tasks...");
        runSimulationSet(tasks100, host1, host2);

        // Reset host resources
        resetHosts(host1, host2);

        // Run simulation for 150 tasks
        System.out.println("\nRunning simulation for 150 tasks...");
        runSimulationSet(tasks150, host1, host2);

        // Reset host resources
        resetHosts(host1, host2);

        // Run simulation for 200 tasks
        System.out.println("\nRunning simulation for 200 tasks...");
        runSimulationSet(tasks200, host1, host2);
    }

    private static void runSimulationSet(List<Task> tasks, Host host1, Host host2) {
        // Create CSA instance
        CSA csa = new CSA(tasks, Arrays.asList(host1, host2));
        
        // Create scheduler
        TaskScheduler scheduler = new TaskScheduler(tasks, Arrays.asList(host1, host2));
        
        // Create and run simulation
        Simulation simulation = new Simulation();
        simulation.runSimulation(tasks, scheduler, csa);
    }

    private static void resetHosts(Host host1, Host host2) {
        host1.usedCpu = 0;
        host1.usedRam = 0;
        host2.usedCpu = 0;
        host2.usedRam = 0;
    }

    private static List<Task> generateTasks(int numTasks) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            // Generate tasks with requirements based on the paper's specifications
            // CPU requirements between 250 and 2000 MIPS
            int cpuRequired = (int) (Math.random() * 1750) + 250;
            
            // RAM requirements between 512 and 2048 MB
            int ramRequired = (int) (Math.random() * 1536) + 512;
            
            // Create task with these requirements
            Task task = new Task(i, cpuRequired, ramRequired);
            tasks.add(task);
        }
        return tasks;
    }
}