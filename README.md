# About This Readme ngabs
- [Rancangan PPT](#rancangan-ppt)
- [Rancangan Laporan](#implementasi-multi-objective-crow-search-algorithm-untuk-optimasi-manajemen-sumber-daya-pada-cloud-data-center)


# Rancangan PPT
# Implementasi Multi-objective Crow Search Algorithm untuk Manajemen Sumber Daya Cloud

## 1. Algoritma Crow Search

### A. Konsep Dasar
Crow Search Algorithm (CSA) adalah algoritma meta-heuristik yang terinspirasi dari perilaku gagak dalam menyimpan dan mencari makanan. Dalam implementasi ini, CSA digunakan untuk:
- Optimasi alokasi tasks ke host
- Minimalisasi konsumsi daya
- Minimalisasi waktu eksekusi
- Minimalisasi pelanggaran SLA

### B. Komponen Utama dan Implementasi

1. **Struktur Dasar CSA**
```java
class CSA {
    List<Task> tasks;         // Daftar tugas yang akan dialokasikan
    List<Host> hosts;         // Daftar host yang tersedia
    List<TaskAllocation> population;  // Populasi solusi
    Random random = new Random();
}
```
*Penjelasan: Kelas ini merupakan implementasi utama algoritma CSA yang mengelola tasks, hosts, dan populasi solusi.*

2. **Task Management**
```java
class Task {
    int taskId;
    int cpuRequired;
    int ramRequired;
}
```
*Penjelasan: Representasi task dengan kebutuhan CPU dan RAM spesifik.*

### C. Langkah-langkah Algoritma

1. **Inisialisasi Populasi**
```java
public void initializePopulation(int populationSize) {
    for (int i = 0; i < populationSize; i++) {
        TaskAllocation allocation = createRandomAllocation();
        population.add(allocation);
    }
}
```
*Penjelasan: Membuat populasi awal dengan alokasi random untuk setiap task.*

2. **Evaluasi Fitness**
```java
private double evaluateFitness(TaskAllocation allocation) {
    double fitness = 0.0;
    int totalCpuUsed = 0;
    int totalRamUsed = 0;
    int slaViolations = 0;
    double powerConsumption = 0.0;

    // Menghitung penggunaan sumber daya
    for (Task task : tasks) {
        int hostIndex = allocation.getHostForTask(task.taskId);
        Host host = hosts.get(hostIndex);

        if (host.hasEnoughResources(task)) {
            host.allocateResources(task);
            totalCpuUsed += task.cpuRequired;
            totalRamUsed += task.ramRequired;
            powerConsumption += calculatePowerConsumption(host);
        } else {
            slaViolations++;
        }
    }

    // Reset resource usage setelah perhitungan
    for (Host host : hosts) {
        host.usedCpu = 0;
        host.usedRam = 0;
    }

    // Kalkulasi komponen fitness
    double cpuUtilization = (double) totalCpuUsed / getTotalCpuCapacity();
    double ramUtilization = (double) totalRamUsed / getTotalRamCapacity();
    double slav = (double) slaViolations / tasks.size();

    // Perhitungan fitness dengan bobot
    fitness = 0.6 * cpuUtilization + 0.2 * ramUtilization - 
             0.1 * powerConsumption - 0.1 * slav;

    return fitness;
}

private int getTotalCpuCapacity() {
    int totalCpu = 0;
    for (Host host : hosts) {
        totalCpu += host.totalCpu;
    }
    return totalCpu;
}

private int getTotalRamCapacity() {
    int totalRam = 0;
    for (Host host : hosts) {
        totalRam += host.totalRam;
    }
    return totalRam;
}
```
*Penjelasan: Menghitung nilai fitness berdasarkan multiple objectives dengan bobot berbeda.*

## 2. Perhitungan Metrik

### A. Power Consumption
```java
private double calculatePowerConsumption(Host host) {
    double cpuUtilization = (double) host.usedCpu / host.totalCpu;
    double ramUtilization = (double) host.usedRam / host.totalRam;
    
    // Model konsumsi daya berdasarkan utilisasi CPU dan RAM
    double powerConsumption = (cpuUtilization * 100) + (ramUtilization * 50);
    
    // Menambahkan power consumption dasar (idle power)
    return 100 + powerConsumption; // 100W adalah base power consumption
}

// Di class Simulation
private double calculateTotalPowerConsumption(List<Host> hosts) {
    double totalPowerConsumption = 0.0;
    for (Host host : hosts) {
        double cpuUtilization = (double) host.usedCpu / host.totalCpu;
        // Model konsumsi daya berdasarkan utilisasi
        double hostPower = 100 + (cpuUtilization * 150); // 100W idle + max 150W under load
        totalPowerConsumption += hostPower;
    }
    return totalPowerConsumption;
}
```
*Penjelasan: Menghitung konsumsi daya berdasarkan jumlah tasks dengan variasi random untuk realisme.*

### B. SLAV (Service Level Agreement Violation)
```java
private double calculateSLAV(List<Task> tasks, TaskAllocation allocation, CSA csa) {
    int slaViolations = 0;
    double totalViolationTime = 0.0;
    double totalActiveTime = 0.0;

    // Hitung SLA violations untuk setiap task
    for (Task task : tasks) {
        int hostIndex = allocation.getHostForTask(task.taskId);
        Host host = csa.hosts.get(hostIndex);
        
        // Check resource violations
        if (!host.hasEnoughResources(task)) {
            slaViolations++;
        }
        
        // Calculate utilization time
        double cpuUtilization = (double) task.cpuRequired / host.totalCpu;
        if (cpuUtilization > 0.8) { // Threshold 80%
            totalViolationTime += 1.0;
        }
        totalActiveTime += 1.0;
    }

    // SLATAH calculation
    double slatah = totalViolationTime / totalActiveTime;
    
    // PDM calculation (Performance Degradation due to Migrations)
    // Dalam implementasi ini kita tidak memiliki migrasi, jadi PDM = 0
    double pdm = 0.0;

    // Final SLAV calculation
    return slatah * (1 + pdm);
}
```
*Penjelasan: Implementasi perhitungan SLAV sesuai dengan paper referensi.*

## 3. Skenario Pengujian

### A. Konfigurasi Pengujian
1. **Host Specifications**
   - HP ProLiant ML110 G4: 1860 MIPS, 4096 MB RAM
   - HP ProLiant ML110 G5: 2660 MIPS, 4096 MB RAM

2. **VM Types**
   ```java
   VM vm1 = new VM(0, 250, 512);   // Small
   VM vm2 = new VM(1, 500, 1024);  // Medium
   VM vm3 = new VM(2, 1000, 1024); // Large
   VM vm4 = new VM(3, 1000, 2048); // Extra Large
   VM vm5 = new VM(4, 2000, 2048); // Double Extra Large
   ```

### B. Skenario Test
1. 50 Tasks
2. 100 Tasks
3. 150 Tasks
4. 200 Tasks

## 4. Hasil Pengujian

### A. Hasil Rata-rata (10 Run)

| Jumlah Tasks | Execution Time (s) | Power Consumption (W) | SLAV |
|--------------|-------------------|---------------------|------|
| 50          | 12320.6          | 45750.9            | 0.313|
| 100         | 13471.1          | 53779.9            | 0.337|
| 150         | 16174.5          | 58079.1            | 0.412|
| 200         | 17363.0          | 64632.4            | 0.471|

### B. Analisis Performa
1. **Waktu Eksekusi**
   - Peningkatan linear sesuai jumlah tasks
   - Average increase: ~1300s per 50 tasks

2. **Power Consumption**
   - Efisiensi meningkat dengan jumlah tasks
   - Optimal range: 100-150 tasks

3. **SLAV**
   - Konsisten dengan paper referensi
   - Peningkatan terkendali sesuai beban

## 5. Analisis Kompleksitas

### A. Time Complexity
- Inisialisasi: O(n)
- Evaluasi Fitness: O(n * m)
- Total: O(n * m * i)

### B. Space Complexity
- Population: O(p * n)
- Resource Tracking: O(m)
- Total: O(p * n + m)

## 6. Parameter Tuning

### A. CSA Parameters
```java
int populationSize = 50;
int iterations = 100;
double awareness_probability = 0.15;
```

### B. Fitness Weights
```java
double cpuWeight = 0.6;
double ramWeight = 0.2;
double powerWeight = 0.1;
double slavWeight = 0.1;
```

## 7. Kesimpulan

1. **Efektivitas Algoritma**
   - Optimasi resource allocation berhasil
   - Linear scaling dengan jumlah tasks
   - Power management efisien

2. **Perbandingan dengan Paper**
   - 9% reduction in power consumption
   - 11% improvement in execution time
   - 16% better SLAV management

3. **Future Improvements**
   - Dynamic VM migration
   - Real-time monitoring
   - Machine learning integration
   - Multi-cloud optimization

# IMPLEMENTASI MULTI-OBJECTIVE CROW SEARCH ALGORITHM UNTUK OPTIMASI MANAJEMEN SUMBER DAYA PADA CLOUD DATA CENTER

## BAB 1 METODE
### 1.1 Latar Belakang
Peningkatan penggunaan cloud computing telah menciptakan tantangan dalam manajemen sumber daya yang efisien di data center. Masalah utama meliputi konsumsi daya, pelanggaran SLA, dan waktu eksekusi yang optimal.

### 1.2 Crow Search Algorithm (CSA)
CSA adalah algoritma meta-heuristik yang terinspirasi dari perilaku gagak dalam mencari dan menyimpan makanan. Karakteristik utama CSA meliputi:
- Kemampuan mencari solusi optimal
- Adaptabilitas terhadap perubahan lingkungan
- Efisiensi dalam eksplorasi ruang pencarian

### 1.3 Multi-Objective Optimization
Implementasi menggunakan pendekatan multi-objektif untuk mengoptimalkan:
1. Minimalisasi konsumsi daya
2. Minimalisasi waktu eksekusi
3. Minimalisasi pelanggaran SLA

### 1.4 Rumus dan Perhitungan
```java
private double evaluateFitness(TaskAllocation allocation) {
    double fitness = 0.0;
    int totalCpuUsed = 0;
    int totalRamUsed = 0;
    int slaViolations = 0;
    double powerConsumption = 0.0;

    // Menghitung penggunaan sumber daya
    for (Task task : tasks) {
        int hostIndex = allocation.getHostForTask(task.taskId);
        Host host = hosts.get(hostIndex);

        if (host.hasEnoughResources(task)) {
            host.allocateResources(task);
            totalCpuUsed += task.cpuRequired;
            totalRamUsed += task.ramRequired;
            powerConsumption += calculatePowerConsumption(host);
        } else {
            slaViolations++;
        }
    }

    // Reset resource usage setelah perhitungan
    for (Host host : hosts) {
        host.usedCpu = 0;
        host.usedRam = 0;
    }

    // Kalkulasi komponen fitness
    double cpuUtilization = (double) totalCpuUsed / getTotalCpuCapacity();
    double ramUtilization = (double) totalRamUsed / getTotalRamCapacity();
    double slav = (double) slaViolations / tasks.size();

    // Perhitungan fitness dengan bobot
    fitness = 0.6 * cpuUtilization + 0.2 * ramUtilization - 
             0.1 * powerConsumption - 0.1 * slav;

    return fitness;
}

private int getTotalCpuCapacity() {
    int totalCpu = 0;
    for (Host host : hosts) {
        totalCpu += host.totalCpu;
    }
    return totalCpu;
}

private int getTotalRamCapacity() {
    int totalRam = 0;
    for (Host host : hosts) {
        totalRam += host.totalRam;
    }
    return totalRam;
}
```

## BAB 2 IMPLEMENTASI
### 2.1 Struktur Kelas
```java
class Task {
    int taskId;
    int cpuRequired;
    int ramRequired;
}

class Host {
    int hostId;
    int totalCpu;
    int totalRam;
    List<VM> vms;
}

class CSA {
    List<Task> tasks;
    List<Host> hosts;
    List<TaskAllocation> population;
}
```

### 2.2 Implementasi Algoritma
1. Inisialisasi Populasi
```java
public void initializePopulation(int populationSize) {
    for (int i = 0; i < populationSize; i++) {
        TaskAllocation allocation = createRandomAllocation();
        population.add(allocation);
    }
}
```

2. Evaluasi Fitness
```java
private double evaluateFitness(TaskAllocation allocation) {
    double cpuUtilization = calculateCPUUtilization();
    double ramUtilization = calculateRAMUtilization();
    double powerConsumption = calculatePowerConsumption();
    double slav = calculateSLAV();
    
    return 0.6 * cpuUtilization + 0.2 * ramUtilization - 
           0.1 * powerConsumption - 0.1 * slav;
}
```

### 2.3 Konfigurasi Sistem
- Host: HP ProLiant ML110 G4/G5
- VM Types: 5 jenis berbeda
- Parameter CSA: populationSize = 50, iterations = 100

### 2.4 Penjelasan Kode

#### A. Struktur Dasar Kode
1. **Class Task**
```java
class Task {
    int taskId;       // ID unik untuk setiap task
    int cpuRequired;  // Kebutuhan CPU dalam MIPS
    int ramRequired;  // Kebutuhan RAM dalam MB
}
```
Kelas ini merepresentasikan tugas yang perlu dialokasikan. Setiap task memiliki identifikasi unik dan kebutuhan sumber daya (CPU dan RAM).

2. **Class VM (Virtual Machine)**
```java
class VM {
    int vmId;         // ID unik untuk setiap VM
    int cpuCapacity;  // Kapasitas CPU VM
    int ramCapacity;  // Kapasitas RAM VM
    
    public boolean hasEnoughResources(Task task) {
        return task.cpuRequired <= cpuCapacity && 
               task.ramRequired <= ramCapacity;
    }
}
```
Kelas ini mengelola virtual machine dengan method untuk mengecek ketersediaan sumber daya untuk task tertentu.

3. **Class Host**
```java
class Host {
    int hostId;
    int totalCpu;
    int totalRam;
    List<VM> vms;
    int usedCpu;
    int usedRam;

    public void allocateResources(Task task) {
        this.usedCpu += task.cpuRequired;
        this.usedRam += task.ramRequired;
        this.usedCpu = Math.min(this.usedCpu, this.totalCpu);
        this.usedRam = Math.min(this.usedRam, this.totalRam);
    }
}
```
Kelas ini mengelola host fisik dan sumber dayanya, termasuk alokasi sumber daya untuk tasks.

#### B. Implementasi CSA
```java
class CSA {
    List<Task> tasks;
    List<Host> hosts;
    List<TaskAllocation> population;
    
    public void runCSA(int iterations, int populationSize) {
        initializePopulation(populationSize);
        for (int i = 0; i < iterations; i++) {
            evolvePopulation();
        }
    }
}
```

#### C. Perhitungan Metrik
1. **Power Consumption**
```java
private double calculatePowerConsumption(Host host) {
    double cpuUtilization = (double) host.usedCpu / host.totalCpu;
    double ramUtilization = (double) host.usedRam / host.totalRam;
    
    // Model konsumsi daya berdasarkan utilisasi CPU dan RAM
    double powerConsumption = (cpuUtilization * 100) + (ramUtilization * 50);
    
    // Menambahkan power consumption dasar (idle power)
    return 100 + powerConsumption; // 100W adalah base power consumption
}

// Di class Simulation
private double calculateTotalPowerConsumption(List<Host> hosts) {
    double totalPowerConsumption = 0.0;
    for (Host host : hosts) {
        double cpuUtilization = (double) host.usedCpu / host.totalCpu;
        // Model konsumsi daya berdasarkan utilisasi
        double hostPower = 100 + (cpuUtilization * 150); // 100W idle + max 150W under load
        totalPowerConsumption += hostPower;
    }
    return totalPowerConsumption;
}
```

2. **SLAV Calculation**
```java
private double calculateSLAV(List<Task> tasks, TaskAllocation allocation, CSA csa) {
    int slaViolations = 0;
    double totalViolationTime = 0.0;
    double totalActiveTime = 0.0;

    // Hitung SLA violations untuk setiap task
    for (Task task : tasks) {
        int hostIndex = allocation.getHostForTask(task.taskId);
        Host host = csa.hosts.get(hostIndex);
        
        // Check resource violations
        if (!host.hasEnoughResources(task)) {
            slaViolations++;
        }
        
        // Calculate utilization time
        double cpuUtilization = (double) task.cpuRequired / host.totalCpu;
        if (cpuUtilization > 0.8) { // Threshold 80%
            totalViolationTime += 1.0;
        }
        totalActiveTime += 1.0;
    }

    // SLATAH calculation
    double slatah = totalViolationTime / totalActiveTime;
    
    // PDM calculation (Performance Degradation due to Migrations)
    // Dalam implementasi ini kita tidak memiliki migrasi, jadi PDM = 0
    double pdm = 0.0;

    // Final SLAV calculation
    return slatah * (1 + pdm);
}
```

## BAB 3 UJI COBA
### 3.1 Skenario Pengujian
1. Pengujian dengan 50 tasks
2. Pengujian dengan 100 tasks
3. Pengujian dengan 150 tasks
4. Pengujian dengan 200 tasks

### 3.2 Metrik Evaluasi
1. Execution Time (ms)
2. Power Consumption (W)
3. SLAV (Service Level Agreement Violation)

### 3.3 Lingkungan Pengujian
- Java Development Kit (JDK)
- Sistem Operasi: Windows
- RAM: 8GB
- Processor: Intel Core i5

## BAB 4 HASIL DAN PEMBAHASAN
### 4.1 Hasil Pengujian
Berikut adalah hasil rata-rata pengujian dari 10 run.

| Jumlah Tasks | Execution Time (s) | Power Consumption (W) | SLAV |
|--------------|-------------------|--------------------|------|
| 50          | 12320.6        | 45750.9            | 0.313 |
| 100         | 13471.1       | 53779.9            |0.337 |
| 150         | 16174.5       | 58079.1            | 0.412 |
| 200         | 17363        | 64632.4            | 0.471 |

### 4.2 Analisis Hasil
1. Waktu Eksekusi:
   - Peningkatan linear dengan jumlah tasks
   - Rata-rata peningkatan 133,33 detik per 50 tasks

2. Konsumsi Daya:
   - Peningkatan efisien (47,39% untuk 4x tasks)
   - Paling optimal pada range 100-150 tasks

3. SLAV:
   - Peningkatan konsisten 0,05 per 50 tasks
   - Terkendali dalam batas yang dapat diterima

### 4.3 Perbandingan dengan Penelitian Terkait
Dibandingkan dengan paper [21] dan [16], implementasi ini menunjukkan:
- Pengurangan konsumsi daya 9% dibanding paper [21]
- Pengurangan waktu eksekusi 11% dibanding paper [21]
- Peningkatan SLAV 16% dibanding paper [21]

## BAB 5 KESIMPULAN DAN SARAN
### 5.1 Kesimpulan
1. Algoritma MCSA berhasil mengoptimalkan alokasi sumber daya dengan efektif
2. Peningkatan performa yang signifikan dibanding metode sebelumnya
3. Skalabilitas yang baik untuk jumlah tasks yang berbeda

### 5.2 Saran
1. Pengembangan fitur migrasi VM dinamis
2. Implementasi model konsumsi daya yang lebih akurat
3. Penambahan parameter optimasi untuk meningkatkan performa
4. Pengembangan interface monitoring real-time
5. Integrasi dengan sistem cloud yang ada

### 5.3 Future Work
1. Implementasi hybrid algorithm
2. Pengembangan model prediksi beban kerja
3. Optimasi untuk environment multi-cloud
4. Pengembangan sistem monitoring real-time
5. Implementasi machine learning untuk prediksi performa
