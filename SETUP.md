# PhotoAI Editor - Kurulum ve Çalıştırma Rehberi

## 📋 Sistem Gereksinimleri

### Windows
- **Android Studio**: Hedgehog (2023.1.1) veya üzeri
- **JDK**: 17 (Android Studio ile birlikte gelir)
- **Minimum RAM**: 8GB (16GB önerilir)
- **Disk Alanı**: En az 10GB boş alan

### macOS
- **Android Studio**: Hedgehog (2023.1.1) veya üzeri
- **macOS**: 10.14 (Mojave) veya üzeri
- **Minimum RAM**: 8GB (16GB önerilir)
- **Disk Alanı**: En az 10GB boş alan

### Linux
- **Android Studio**: Hedgehog (2023.1.1) veya üzeri
- **64-bit dağıtım**
- **Minimum RAM**: 8GB (16GB önerilir)
- **Disk Alanı**: En az 10GB boş alan

---

## 🚀 Adım Adım Kurulum

### 1. Android Studio İndirme ve Kurma

#### Windows:
1. https://developer.android.com/studio adresine gidin
2. "Download Android Studio" butonuna tıklayın
3. İndirilen `.exe` dosyasını çalıştırın
4. Kurulum sihirbazını takip edin
5. İlk açılışta "Standard" kurulum seçin
6. SDK bileşenlerinin indirilmesini bekleyin

#### macOS:
1. https://developer.android.com/studio adresine gidin
2. "Download Android Studio" butonuna tıklayın
3. İndirilen `.dmg` dosyasını açın
4. Android Studio'yu Applications klasörüne sürükleyin
5. İlk açılışta "Standard" kurulum seçin

#### Linux:
```bash
# Ubuntu/Debian
sudo apt-get install android-studio

# Ya da manuel:
# 1. https://developer.android.com/studio adresinden tar.gz indir
# 2. Arşivi çıkart:
tar -xvf android-studio-*.tar.gz
# 3. Studio'yu başlat:
cd android-studio/bin
./studio.sh
```

### 2. Projeyi Açma

#### Yöntem 1: Android Studio İçinden
1. Android Studio'yu açın
2. "Open" butonuna tıklayın (veya File > Open)
3. Projenizin olduğu klasörü seçin: `/home/user/denandv1`
4. "OK" butonuna tıklayın

#### Yöntem 2: Terminalden
```bash
cd /home/user/denandv1

# Windows (Git Bash)
start "" "C:\Program Files\Android Studio\bin\studio64.exe" .

# macOS
open -a "Android Studio" .

# Linux
/path/to/android-studio/bin/studio.sh .
```

### 3. İlk Sync ve Bekleme

Proje ilk açıldığında:

1. **Gradle Sync** otomatik başlayacak (ekranın altında progress bar)
2. Bu işlem **5-15 dakika** sürebilir (internet hızınıza bağlı)
3. Aşağıdaki bileşenler indirilecek:
   - Gradle 8.2
   - Android SDK 34
   - Build tools
   - Kotlin plugin
   - Tüm dependency'ler (Compose, Hilt, AdMob, vb.)

**ÖNEMLI**: İlk sync tamamlanana kadar hiçbir işlem yapmayın!

**Sync Durumu Kontrol**:
- Altta "Gradle: Build" yazısı kaybolmalı
- Yeşil tik işareti görünmeli
- "BUILD SUCCESSFUL" mesajı çıkmalı

### 4. SDK Kontrol ve Kurulum

Gradle sync'ten sonra, SDK'ların kurulu olup olmadığını kontrol edin:

1. **Tools > SDK Manager** menüsüne gidin
2. **SDK Platforms** sekmesinde:
   - ✅ Android 14.0 (API Level 34) - İşaretli olmalı
   - ✅ Android 7.0 (API Level 24) - İşaretli olmalı

3. **SDK Tools** sekmesinde:
   - ✅ Android SDK Build-Tools 34
   - ✅ Android Emulator
   - ✅ Android SDK Platform-Tools

4. Eksik varsa "Apply" butonuna tıklayın ve indirmeyi bekleyin

### 5. Emulator Kurulum (Fiziksel Cihaz Yoksa)

#### Yeni Emulator Oluşturma:
1. **Tools > Device Manager** açın
2. "Create Device" butonuna tıklayın
3. **Phone > Pixel 6** seçin (önerilir)
4. "Next" tıklayın
5. **System Image**: "UpsideDownCake" (API 34) seçin
   - İlk kez kuruyorsanız "Download" linkine tıklayın
   - İndirme tamamlandıktan sonra seçin
6. "Next" > "Finish"

#### Emulator Ayarları (Performans için):
1. Oluşturulan emulator'un yanındaki ⚙️ (düzenle) ikonuna tıklayın
2. **Performance**:
   - Graphics: Hardware - GLES 2.0
   - Boot option: Cold boot (ilk açılış için)
3. **RAM**: 4096 MB (4 GB)
4. "Finish"

### 6. Proje Yapılandırması (İsteğe Bağlı - Production için)

#### AdMob Ayarları (Şimdilik Test ID'ler var):
```kotlin
// app/build.gradle.kts - 38. satır
manifestPlaceholders["admobAppId"] = "ca-app-pub-XXXXX~YYYY" // Kendi ID'nizi girin

// app/src/main/java/com/photoai/editor/data/ads/AdManager.kt - 32-33. satırlar
const val BANNER_AD_UNIT_ID = "ca-app-pub-XXXXX/YYYY"
const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-XXXXX/YYYY"
```

#### Google Play Billing Ayarları:
```kotlin
// app/src/main/java/com/photoai/editor/data/billing/BillingManager.kt - 43. satır
private const val PREMIUM_MONTHLY_PRODUCT_ID = "your_product_id_from_play_console"
```

### 7. Uygulamayı Çalıştırma

#### Yöntem 1: Emulator ile
1. Üstte cihaz seçim dropdown'ından emulator'ü seçin
2. Yeşil ▶️ (Run) butonuna tıklayın
3. İlk çalıştırma **2-3 dakika** sürer
4. Uygulama emulator'de açılacak

#### Yöntem 2: Fiziksel Cihaz ile
1. **Cihazda Geliştirici Seçeneklerini Aktifleştirin**:
   - Ayarlar > Telefon Hakkında
   - "Yapı Numarası"na 7 kez tıklayın
   - "Geliştirici oldunuz!" mesajı çıkacak

2. **USB Debugging Aktif Edin**:
   - Ayarlar > Geliştirici Seçenekleri
   - "USB Debugging" açın

3. **Cihazı Bağlayın**:
   - USB kablosuyla bilgisayara bağlayın
   - Cihazda "Bu bilgisayara izin ver" onaylayın

4. **Android Studio'da**:
   - Üstte cihaz seçim dropdown'ında cihazınız görünecek
   - Yeşil ▶️ (Run) butonuna tıklayın

---

## 🐛 Sık Karşılaşılan Sorunlar ve Çözümler

### Sorun 1: "Gradle sync failed"
**Çözüm**:
```bash
# 1. Gradle cache temizle
./gradlew clean

# 2. Android Studio'yu kapatın
# 3. .gradle klasörünü silin:
rm -rf ~/.gradle/caches

# 4. Android Studio'yu tekrar açın ve sync yapın
```

### Sorun 2: "SDK location not found"
**Çözüm**:
1. File > Project Structure > SDK Location
2. Android SDK location'ı manuel girin:
   - Windows: `C:\Users\<username>\AppData\Local\Android\Sdk`
   - macOS: `/Users/<username>/Library/Android/sdk`
   - Linux: `/home/<username>/Android/Sdk`

### Sorun 3: "Emulator bulunamıyor"
**Çözüm**:
```bash
# Emulator yolunu PATH'e ekleyin:

# Windows (System Environment Variables):
C:\Users\<username>\AppData\Local\Android\Sdk\emulator
C:\Users\<username>\AppData\Local\Android\Sdk\platform-tools

# macOS/Linux (~/.bashrc veya ~/.zshrc):
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

### Sorun 4: "Insufficient memory" hatası
**Çözüm**:
```bash
# gradle.properties dosyasına ekleyin:
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=512m -XX:+HeapDumpOnOutOfMemoryError
```

### Sorun 5: Build çok yavaş
**Çözüm**:
```bash
# gradle.properties dosyasına ekleyin:
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.configureondemand=true
```

### Sorun 6: "Unresolved reference" hataları
**Çözüm**:
1. File > Invalidate Caches > Invalidate and Restart
2. Build > Clean Project
3. Build > Rebuild Project

---

## ✅ Test Checklist

Uygulamayı çalıştırdıktan sonra test edin:

- [ ] Splash ekranı görünüyor
- [ ] Onboarding 3 sayfa gösteriyor
- [ ] Home ekranına geçiş yapılıyor
- [ ] "Select from Gallery" butonu çalışıyor
- [ ] Galeri açılıyor ve fotoğraf seçilebiliyor
- [ ] Edit ekranına fotoğraf yükleniyor
- [ ] Filtreler gösteriliyor (6 adet)
- [ ] Filtreye tıklayınca loading gösteriliyor
- [ ] Filtre uygulanıyor (görsel değişiyor)
- [ ] Premium filterda "Premium required" mesajı çıkıyor
- [ ] Premium ekranı açılıyor
- [ ] Banner reklam gösteriliyor (test reklamı)
- [ ] Save butonu çalışıyor
- [ ] Galeri'de fotoğraf görünüyor (filigranla)

---

## 📱 İlk Kullanım Akışı

1. **Uygulama açılır** → Splash screen (2 saniye)
2. **Onboarding** → 3 sayfa tanıtım
3. **Home Screen** → "Select from Gallery" butonuna tıklayın
4. **Galeri** → Bir fotoğraf seçin
5. **Edit Screen** → Filtrelerden birini seçin
6. **Processing** → 1-2 saniye bekleyin
7. **Result** → Düzenlenmiş fotoğraf görünür
8. **Save** → Kaydet butonuna tıklayın
9. **Galeri'den kontrol** → Fotoğraf kaydedildi

---

## 🔧 Geliştirici Modunda Çalıştırma

Detaylı log görmek için:

```bash
# Logcat'i açın
adb logcat | grep "PhotoAI"

# Veya Android Studio'da:
# View > Tool Windows > Logcat
# Filter'a "PhotoAI" yazın
```

---

## 📞 Yardım

Sorun yaşıyorsanız:

1. Bu dosyayı baştan okuyun
2. "Sık Karşılaşılan Sorunlar" bölümünü kontrol edin
3. Android Studio'nun en son versiyonunu kullandığınızdan emin olun
4. GitHub Issues açın: [Proje Issue Sayfası]

---

## 🎉 Başarılı Kurulum!

Artık PhotoAI Editor'ı çalıştırmaya hazırsınız!

**Sonraki adımlar**:
- README.md dosyasını okuyun (proje yapısı için)
- Kodları incelemeye başlayın
- Kendi özelliklerinizi ekleyin
- AI API entegrasyonu yapın

**İyi kodlamalar! 🚀**
