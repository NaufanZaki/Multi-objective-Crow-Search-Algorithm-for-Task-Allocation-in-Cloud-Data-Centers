# Project Summary: Multi-objective Crow Search Algorithm for Task Allocation in Cloud Data Centers

## Objective
This project is based on the paper *"Providing a Solution for Optimal Management of Resources using the Multi-objective Crow Search Algorithm in Cloud Data Centers"* by Nastaran Evaznia and Reza Ebrahimi (2023), which addresses the efficient allocation of cloud resources using the Crow Search Algorithm (CSA). The goal is to minimize resource wastage and improve service-level agreement (SLA) compliance by optimally distributing tasks to cloud virtual machines (VMs).

## Key Criteria from the Paper
The project follows the specifications outlined in the paper, including:
- **Task Specifications:**
  - Task length ranges from 1000 to 20000
  - The total number of tasks: 100-1000
  - Each task has CPU and RAM requirements
- **Virtual Machine (VM) Specifications:**
  - RAM: 256MB to 2048MB
  - CPU: 500MIPS to 2000MIPS
- **Cloud Data Center Specifications:**
  - Number of data centers: 10
  - Hosts per data center: 2-6
  - VMs per host: 1-4
- **Task Allocation Criteria:**
  - Tasks must be allocated to hosts based on available resources (CPU and RAM)
  - The allocation must minimize energy consumption, execution time, and SLA violations

## Project Process
1. **Data Modeling and Setup:**
   - Virtual machines (VMs) with varying CPU and RAM capacities are set up in hosts.
   - Tasks are generated with random CPU and RAM requirements within a reasonable range.
   - The CSA is applied to allocate tasks to VMs in a way that minimizes SLA violations and resource wastage.
   
2. **Crow Search Algorithm (CSA) Implementation:**
   - CSA simulates the behavior of crows to explore and exploit the solution space.
   - Population of possible task-to-host allocations is initialized and evaluated based on fitness (CPU and RAM utilization, SLA compliance).
   - The algorithm iterates over 100 generations to optimize task allocation.
   
3. **Simulation and Testing:**
   - The simulation is run with task sets of 50, 100, 150, and 200 tasks.
   - The performance is evaluated based on the execution time of the allocation and the resulting fitness.

## Output
- The CSA successfully allocates tasks to cloud hosts, with the best fitness solution chosen after 100 iterations.
- The output for each task set includes:
  - The task-to-host allocation for each task
  - The fitness score, representing the quality of the allocation (i.e., how well resources are utilized and how many SLA violations occurred).
  
- The output also reports execution times for the simulations, indicating the efficiency of the CSA in optimizing the task allocation.

## Analysis
The project shows that CSA can efficiently allocate tasks in cloud environments, minimizing resource wastage and improving SLA compliance. However, the fitness values and task allocations are sensitive to the initial configuration of tasks and VMs. The performance improves with more tasks, but the optimization process remains computationally intensive.

### Key Metrics:
- **Best Fitness Values:**
  - For 50 tasks: -26,979
  - For 100 tasks: -46,956
  - For 150 tasks: -82,945
  - For 200 tasks: -93,908
- **Execution Time:**
  - For 50 tasks: 42 ms
  - For 100 tasks: 4 ms
  - For 150 tasks: 7 ms
  - For 200 tasks: 6 ms

These results indicate that the algorithm can handle varying numbers of tasks and still produce reasonable allocations within acceptable time frames.

## Conclusion
The project demonstrates the effectiveness of using the Crow Search Algorithm to optimize resource allocation in cloud data centers. By minimizing SLA violations and improving resource utilization, CSA offers a promising solution for cloud resource management.

## Citation
Evaznia, N., & Ebrahimi, R. (2023). *Providing a Solution for Optimal Management of Resources using the Multi-objective Crow Search Algorithm in Cloud Data Centers*. 9th International Conference on Web Research (ICWR), 179.
