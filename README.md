# 🍄 MyShroom  
**Pengklasifikasi Spesies Jamur Menggunakan Deep Learning (CNN MobileNetV2)**

![main_logo](https://github.com/user-attachments/assets/464f9507-f2dd-476c-835e-c21e63c4bf8d)

---

## 📱 Tentang Aplikasi

**MyShroom** adalah aplikasi Android berbasis Deep Learning yang digunakan untuk mengenali atau mengklasifikasi spesies jamur menggunakan arsitektur **CNN MobileNetV2**.  
Aplikasi ini bertujuan memberikan informasi terkait **keamanan konsumsi jamur**, sehingga pengguna dapat mengetahui apakah jamur yang ditemukan **aman dikonsumsi atau beracun**.

---

## 🔎 Spesies Jamur yang Dapat Dikenali

MyShroom mampu mengenali **10 jenis spesies jamur**, di antaranya:

1. *Ganoderma lucidum*  
2. *Auricularia auricula-judae*  
3. *Pleurotus ostreatus*  
4. *Agaricus bisporus*  
5. *Flammulina filiformis*  
6. *Amanita phalloides* ⚠️  
7. *Galerina marginata* ⚠️  
8. *Gyromitra esculenta* ⚠️  
9. *Amanita muscaria* ⚠️  
10. *Amanita pantherina* ⚠️  

> ⚠️ Jamur dengan tanda ini merupakan jenis **beracun** atau **berpotensi membahayakan**.

---

## 🖼️ Preview Aplikasi

Berikut adalah tampilan dari halaman-halaman utama di aplikasi MyShroom:  
![Preview Image](https://github.com/user-attachments/assets/1f8ff427-1da3-4dc4-9598-5eb61cf85be6)

---

## 🌟 Fitur Unggulan

- **🔐 Login**  
  Login menggunakan **email dan password**. Menggunakan layanan **Supabase Auth** dan **PostgreSQL**.

- **📝 Register**  
  Pendaftaran akun baru dengan data **email, nama, password, dan konfirmasi password**.

- **🏠 Home**  
  Menampilkan:
  - Daftar spesies jamur
  - Akses halaman koleksi
  - Tombol klasifikasi
  - Logout

- **🗂️ Collections**  
  Riwayat klasifikasi spesies jamur berdasarkan gambar. Klik gambar untuk melihat detail.

- **📷 Klasifikasi**  
  Pilih gambar dari **kamera** atau **galeri**, lalu tekan "Klasifikasi".

- **🔍 Mushroom Detail**  
  Hasil klasifikasi spesies jamur. Juga bisa diakses dari halaman Collections.

---

## 🎥 Demo

Tonton demo penggunaan aplikasi:  

https://github.com/user-attachments/assets/a568c1ab-cc5b-453b-866f-cd1fe5867e03



---

## 🛠️ Teknologi yang Digunakan

- **Jetpack Compose** – UI modern Android  
- **Supabase | PostgreSQL | Realtime Database** – Backend & autentikasi  
- **Dagger Hilt** – Dependency injection  
- **MVVM Architecture** – Clean architecture pattern  
- **TensorFlow Lite** – Model klasifikasi  
- **Material 3** – UI design system modern

---
