public class RiwayatKunjungan {

    private static class Node {
        String catatan;   // isi catatan kunjungan
        Node prev;        // pointer ke kunjungan sebelumnya
        Node next;        // pointer ke kunjungan berikutnya

        Node(String catatan) {
            this.catatan = catatan;
        }
    }

    private Node head;     // kunjungan paling awal
    private Node tail;     // kunjungan paling akhir
    private Node saatIni;  // kunjungan yang sedang dilihat dokter
    private int jumlah;    // banyaknya kunjungan
    private int posisi;    // nomor urut kunjungan saat ini (1..jumlah)

    public void tambahKunjungan(String catatan) {
        Node baru = new Node(catatan);
        if (head == null) {          // riwayat masih kosong
            head = baru;
            tail = baru;
        } else {                     // sambungkan di belakang tail
            tail.next = baru;
            baru.prev = tail;
            tail = baru;
        }
        jumlah++;
        saatIni = baru;
        posisi = jumlah;
    }

    public boolean sebelumnya() {
        if (saatIni == null || saatIni.prev == null) {
            return false;
        }
        saatIni = saatIni.prev;
        posisi--;
        return true;
    }

    public boolean berikutnya() {
        if (saatIni == null || saatIni.next == null) {
            return false;
        }
        saatIni = saatIni.next;
        posisi++;
        return true;
    }

    public void tampilkanSaatIni() {
        if (saatIni == null) {
            System.out.println("   Saat ini : (riwayat masih kosong)");
            return;
        }
        System.out.println("   Saat ini : [" + posisi + "/" + jumlah + "] " + saatIni.catatan);
    }

    public void tampilkanSemua() {
        Node p = head;
        int nomor = 1;
        while (p != null) {
            String penanda = (p == saatIni) ? "  <-- saat ini" : "";
            System.out.println("     " + nomor + ". " + p.catatan + penanda);
            p = p.next;
            nomor++;
        }
    }

    public boolean hapusSaatIni() {
        if (saatIni == null) {
            return false;
        }
        Node target = saatIni;
        Node kiri = target.prev;
        Node kanan = target.next;

        if (kiri != null) kiri.next = kanan; else head = kanan;  // sambung kiri -> kanan
        if (kanan != null) kanan.prev = kiri; else tail = kiri;  // sambung kanan -> kiri

        target.prev = null;   // putus pointer node lama agar bersih
        target.next = null;
        jumlah--;

        if (kanan != null) {          // nomor posisi tidak berubah: node kanan menempati nomornya
            saatIni = kanan;
        } else {                      // yang dihapus node terakhir, mundur ke kiri
            saatIni = kiri;
            posisi--;
        }
        return true;
    }
    public static void main(String[] args) {
        RiwayatKunjungan riwayat = new RiwayatKunjungan();

        System.out.println("=== SIMULASI RIWAYAT KUNJUNGAN PASIEN (Doubly Linked List) ===");
        System.out.println();

        String[] daftar = {
            "05-01-2026 | Demam & batuk         | dr. Andi  | Resep: Paracetamol",
            "12-02-2026 | Kontrol tekanan darah | dr. Sari  | Resep: Amlodipine 5 mg",
            "20-03-2026 | Nyeri lambung         | dr. Budi  | Resep: Omeprazole",
            "15-04-2026 | Cek gula darah        | dr. Sari  | Resep: Metformin"
        };

        int langkah = 1;
        for (int i = 0; i < daftar.length; i++) {
            riwayat.tambahKunjungan(daftar[i]);
            System.out.println("Langkah " + langkah++ + " - tambahKunjungan() ke-" + (i + 1));
            riwayat.tampilkanSaatIni();
        }

        System.out.println();
        for (int i = 1; i <= 2; i++) {
            boolean ok = riwayat.sebelumnya();
            System.out.println("Langkah " + langkah++ + " - sebelumnya() #" + i + (ok ? "" : "  (gagal: sudah di awal)"));
            riwayat.tampilkanSaatIni();
        }

        System.out.println();
        boolean ok = riwayat.berikutnya();
        System.out.println("Langkah " + langkah++ + " - berikutnya() #1" + (ok ? "" : "  (gagal: sudah di akhir)"));
        riwayat.tampilkanSaatIni();

        System.out.println();
        System.out.println("Kondisi akhir seluruh riwayat:");
        riwayat.tampilkanSemua();

        System.out.println();
        System.out.println("=== UJI TAMBAHAN: batas navigasi & hapus kunjungan salah input ===");
        while (riwayat.sebelumnya()) { /* mundur sampai kunjungan pertama */ }
        System.out.println("Mundur terus sampai paling awal:");
        riwayat.tampilkanSaatIni();
        System.out.println("Coba sebelumnya() lagi -> " + (riwayat.sebelumnya() ? "berhasil" : "ditolak, sudah di kunjungan pertama"));

        riwayat.berikutnya();   // pindah ke kunjungan ke-2 (anggap salah input)
        System.out.println("Pindah ke kunjungan ke-2 lalu hapusSaatIni():");
        riwayat.hapusSaatIni();
        riwayat.tampilkanSaatIni();
        System.out.println("Riwayat setelah penghapusan:");
        riwayat.tampilkanSemua();
        System.out.println("Navigasi tetap utuh -> sebelumnya():");
        riwayat.sebelumnya();
        riwayat.tampilkanSaatIni();
    }
}
