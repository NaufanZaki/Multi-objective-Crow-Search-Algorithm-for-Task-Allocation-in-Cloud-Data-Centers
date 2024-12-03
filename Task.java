import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Task {
    int taskId;
    int cpuRequired; // in MIPS
    int ramRequired; // in MB

    public Task(int taskId, int cpuRequired, int ramRequired) {
        this.taskId = taskId;
        this.cpuRequired = cpuRequired;
        this.ramRequired = ramRequired;
    }
}

class VM {
    int vmId;
    int cpuCapacity; // in MIPS
    int ramCapacity; // in MB

    public VM(int vmId, int cpuCapacity, int ramCapacity) {
        this.vmId = vmId;
        this.cpuCapacity = cpuCapacity;
        this.ramCapacity = ramCapacity;
    }

    public boolean hasEnoughResources(Task task) {
        return task.cpuRequired <= cpuCapacity && task.ramRequired <= ramCapacity;
    }
}

class Host {
    int hostId;
    int totalCpu; // in MIPS
    int totalRam; // in MB
    List<VM> vms; // List of VMs in the host

    public Host(int hostId, int totalCpu, int totalRam) {
        this.hostId = hostId;
        this.totalCpu = totalCpu;
        this.totalRam = totalRam;
        this.vms = new ArrayList<>();
    }

    public void addVM(VM vm) {
        vms.add(vm);
    }
}

class CSA {
    List<Task> tasks;
    List<Host> hosts;
    List<TaskAllocation> population;
    Random random = new Random();

    public CSA(List<Task> tasks, List<Host> hosts) {
        this.tasks = tasks;
        this.hosts = hosts;
        this.population = new ArrayList<>();
    }

    // Initialize population with random task-to-VM allocations
    public void initializePopulation(int populationSize) {
        if (populationSize <= 0) {
            System.out.println("Population size must be greater than 0.");
            return;
        }

        for (int i = 0; i < populationSize; i++) {
            TaskAllocation allocation = createRandomAllocation();
            population.add(allocation);
        }

        System.out.println("Population initialized with size: " + population.size());  // Debugging line

        if (population.isEmpty()) {
            System.out.println("Population is empty after initialization!");
        }
    }


    private TaskAllocation createRandomAllocation() {
        TaskAllocation allocation = new TaskAllocation(tasks.size());  // Ensure the size is based on the current task list
        for (Task task : tasks) {
            int hostIndex = random.nextInt(hosts.size());  // Ensure this index is within bounds
            allocation.allocateTask(task, hostIndex);
        }
        return allocation;
    }


    private double evaluateFitness(TaskAllocation allocation) {
        double fitness = 0.0;
        int totalCpuUsed = 0;
        int totalRamUsed = 0;
        int slaViolations = 0;

        // Evaluate fitness based on CPU and RAM utilization
        for (Task task : tasks) {
            int hostIndex = allocation.getHostForTask(task.taskId);
            Host host = hosts.get(hostIndex);
            VM vm = host.vms.get(0);  // Simplification: use the first VM

            if (vm.hasEnoughResources(task)) {
                totalCpuUsed += task.cpuRequired;
                totalRamUsed += task.ramRequired;
            } else {
                slaViolations++;  // Increment if the task cannot be allocated
            }
        }
        
        // Penalize for SLA violations
        fitness -= slaViolations * 1000;
        fitness += (totalCpuUsed + totalRamUsed) / 1000;  // Reward lower resource usage
        return fitness;
    }


    public void runCSA(int iterations, int populationSize) {
        // Initialize population before starting the CSA iterations
        initializePopulation(populationSize);  // Ensure population is initialized

        // Adjust population size based on the number of tasks
        int dynamicPopulationSize = Math.min(populationSize, tasks.size());
        
        // Ensure population is initialized before proceeding
        if (population.isEmpty()) {
            System.out.println("Population is still empty. Aborting the CSA run.");
            return;
        }

        for (int i = 0; i < iterations; i++) {
            List<TaskAllocation> newPopulation = new ArrayList<>();

            // Check if newPopulation has valid solutions before accessing it
            for (TaskAllocation solution : population) {
                double fitness = evaluateFitness(solution);
                
                // Add solution to new population if it's better than the current best
                if (newPopulation.isEmpty() || fitness > evaluateFitness(newPopulation.get(0))) {
                    newPopulation.add(solution);  // Add to new population if it's the best
                }
            }

            // Check if newPopulation is not empty before updating the population
            if (!newPopulation.isEmpty()) {
                // If the population exceeds the dynamic size, trim it
                if (newPopulation.size() > dynamicPopulationSize) {
                    newPopulation = newPopulation.subList(0, dynamicPopulationSize);
                }
                population = newPopulation;  // Update population with the new best solutions
            } else {
                System.out.println("No valid solutions in newPopulation. Exiting CSA run.");
                return;
            }

            // Optionally log the fitness of the best solution in each iteration for tracking
            double bestFitness = evaluateFitness(newPopulation.get(0));
            System.out.println("Iteration " + (i + 1) + " - Best Fitness: " + bestFitness);
        }
    }



    // Get the best task allocation from the population
    public TaskAllocation getBestSolution() {
        if (population.isEmpty()) {
            System.out.println("Population is empty. No solution found.");
            return null;
        }
        return population.get(0); // Return the best allocation (simplified)
    }
}


class TaskScheduler {
    List<Task> tasks;
    List<Host> hosts;

    public TaskScheduler(List<Task> tasks, List<Host> hosts) {
        this.tasks = tasks;
        this.hosts = hosts;
    }

    public void allocateTasks(CSA csa) {
        // Use CSA to allocate tasks
        csa.runCSA(100, 50); // Run CSA for 100 iterations with a population of 50
        TaskAllocation bestAllocation = csa.getBestSolution();

        for (Task task : tasks) {
            int hostIndex = bestAllocation.getHostForTask(task.taskId);
            System.out.println("Task " + task.taskId + " allocated to Host " + hostIndex);
        }
    }
}

class TaskAllocation {
    private int[] taskToHost;

    // Constructor: Initialize array size based on the number of tasks
    public TaskAllocation(int numTasks) {
        taskToHost = new int[numTasks];
    }

    // Allocate a task to a host by assigning the taskId to the hostId
    public void allocateTask(Task task, int hostId) {
        taskToHost[task.taskId] = hostId;  // Ensure taskId is within bounds
    }

    // Get the host assigned to the task by taskId
    public int getHostForTask(int taskId) {
        return taskToHost[taskId];
    }
}


class Simulation {
    public void runSimulation(List<Task> tasks, TaskScheduler scheduler, CSA csa) {
        long startTime = System.nanoTime();
        scheduler.allocateTasks(csa); // Use CSA-based task allocation
        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1000000; // in milliseconds
        System.out.println("Execution time: " + executionTime + " ms");
    }
}
