# Manajemen Sumber Daya Cloud menggunakan Multi-objective Crow Search Algorithm

## Gambaran Proyek
Proyek ini mengimplementasikan solusi manajemen sumber daya untuk pusat data cloud menggunakan Multi-objective Crow Search Algorithm (MCSA). Implementasi ini berfokus pada optimasi penjadwalan tugas dan alokasi sumber daya untuk meminimalkan konsumsi daya, waktu eksekusi, dan Pelanggaran Service Level Agreement (SLAV).

## Detail Algoritma

### Crow Search Algorithm (CSA)
CSA adalah algoritma optimasi yang terinspirasi dari perilaku cerdas burung gagak dalam menyimpan dan mengambil makanan. Dalam implementasi ini, algoritma digunakan untuk penjadwalan tugas dengan komponen berikut:

1. **Inisialisasi Populasi**
   - Setiap solusi merepresentasikan kemungkinan alokasi tugas ke host
   - Alokasi awal dibuat secara acak untuk populasi

2. **Fungsi Fitness**
   Fungsi multi-objektif mempertimbangkan:
   - Penggunaan CPU
   - Penggunaan RAM
   - Konsumsi daya
   - Pelanggaran SLA
   
   Rumus: `fitness = 0.6 * cpuUtilization + 0.2 * ramUtilization - 0.1 * powerConsumption - 0.1 * slav`

3. **Alokasi Sumber Daya**
   - Tugas dialokasikan ke host berdasarkan ketersediaan sumber daya
   - Setiap host melacak penggunaan CPU dan RAM
   - Alokasi mencegah kelebihan beban sumber daya

## Detail Implementasi

### Struktur Kelas
1. **Task Class**
   - Merepresentasikan tugas individual dengan kebutuhan CPU dan RAM
   - Parameter: taskId, cpuRequired (MIPS), ramRequired (MB)

2. **VM Class**
   - Merepresentasikan virtual machine dengan kapasitas sumber daya
   - Parameter: vmId, cpuCapacity, ramCapacity

3. **Host Class**
   - Merepresentasikan server fisik
   - Mengelola beberapa VM
   - Melacak penggunaan sumber daya

4. **CSA Class**
   - Mengimplementasikan Crow Search Algorithm
   - Mengelola populasi dan evolusi
   - Mengevaluasi solusi

### Spesifikasi Perangkat Keras
- **Konfigurasi Host**
  - HP ProLiant ML110 G4: 1860 MIPS, 4096 MB RAM
  - HP ProLiant ML110 G5: 2660 MIPS, 4096 MB RAM

- **Tipe VM**
  ```
  VM1: 250 MIPS, 512 MB
  VM2: 500 MIPS, 1024 MB
  VM3: 1000 MIPS, 1024 MB
  VM4: 1000 MIPS, 2048 MB
  VM5: 2000 MIPS, 2048 MB
  ```

## Hasil Eksperimen

### Skenario Pengujian
Pengujian dilakukan dengan jumlah tugas berbeda:
- 50 tugas
- 100 tugas
- 150 tugas
- 200 tugas

### Metrik Kinerja

1. **Waktu Eksekusi**
   ```
   50 tugas:  1200 detik
   100 tugas: 1300 detik
   150 tugas: 1500 detik
   200 tugas: 1600 detik
   ```
   - Menunjukkan peningkatan linear dengan jumlah tugas
   - Mendemonstrasikan penggunaan sumber daya yang efisien

2. **Konsumsi Daya**
   ```
   50 tugas:  443,48 Watt
   100 tugas: 514,33 Watt
   150 tugas: 572,49 Watt
   200 tugas: 653,66 Watt
   ```
   - Meningkat seiring beban tugas
   - Menunjukkan manajemen daya yang efisien

3. **SLAV (Pelanggaran Service Level Agreement)**
   ```
   50 tugas:  0,30
   100 tugas: 0,35
   150 tugas: 0,40
   200 tugas: 0,45
   ```
   - Peningkatan bertahap dengan jumlah tugas
   - Mengindikasikan penanganan kontentsi sumber daya

## Analisis

### Efektivitas Algoritma
1. **Penggunaan Sumber Daya**
   - Distribusi tugas yang seimbang antar host
   - Penggunaan sumber daya yang tersedia secara efektif

2. **Skalabilitas Kinerja**
   - Peningkatan waktu eksekusi yang linear
   - Peningkatan konsumsi daya yang dapat diprediksi
   - Perkembangan SLAV yang dapat dikelola

3. **Tujuan Optimasi**
   - Berhasil meminimalkan konsumsi daya
   - Mempertahankan waktu eksekusi yang wajar
   - Mengontrol SLAV dalam batas yang dapat diterima

### Temuan Utama
1. MCSA secara efektif menyeimbangkan beberapa objektif:
   - Penggunaan sumber daya
   - Efisiensi daya
   - Pemeliharaan level layanan

2. Metrik kinerja menunjukkan:
   - Skalabilitas yang dapat diprediksi dengan peningkatan beban
   - Alokasi sumber daya yang efisien
   - Manajemen daya yang efektif

3. Algoritma mendemonstrasikan:
   - Stabilitas di berbagai beban tugas
   - Pola kinerja yang konsisten
   - Manajemen sumber daya yang dapat diandalkan

## Penggunaan

### Persyaratan
- Java Development Kit (JDK)
- Memori sistem yang cukup untuk simulasi

### Menjalankan Simulasi
1. Kompilasi semua file Java
2. Jalankan Main class
3. Hasil akan menampilkan:
   - Alokasi tugas
   - Waktu eksekusi
   - Konsumsi daya
   - Metrik SLAV

### Konfigurasi
- Sesuaikan jumlah tugas di Main class
- Modifikasi spesifikasi host/VM sesuai kebutuhan
- Konfigurasi parameter CSA untuk optimasi

## Pengembangan Masa Depan
1. Dukungan migrasi VM dinamis
2. Optimasi sumber daya real-time
3. Objektif optimasi tambahan
4. Model konsumsi daya yang ditingkatkan
5. Prediksi SLAV yang ditingkatkan

## Referensi
Berdasarkan paper penelitian: "Providing a Solution for Optimal Management of Resources using the Multi-objective Crow Search Algorithm in Cloud Data Centers" (2023 9th International Conference on Web Research)