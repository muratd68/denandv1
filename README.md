# PhotoAI Editor - AI-Powered Photo Editing App

Modern Android fotoğraf düzenleme uygulaması. Yapay zeka destekli filtreler, premium abonelik ve reklam entegrasyonu ile tam özellikli bir mobil uygulama.

## 🎯 Özellikler

### Ana Özellikler
- ✨ **AI Filtreler**: Enhance, Beautify, Portrait HD, Cartoon, Vintage, B&W
- 📸 **Galeri Entegrasyonu**: Galeriden fotoğraf seçimi
- 💾 **Fotoğraf Kaydetme**: Düzenlenmiş fotoğrafları galeriye kaydetme
- 🔄 **Önce/Sonra Karşılaştırma**: Press-and-hold ile orijinal/düzenlenmiş karşılaştırma
- 💎 **Premium Abonelik**: Google Play Billing entegrasyonu
- 📱 **AdMob Reklamları**: Banner ve interstitial reklamlar
- 💧 **Filigran Sistemi**: Free kullanıcılar için otomatik filigran

### Teknik Özellikler
- **Clean Architecture**: Domain, Data, Presentation katmanları
- **MVVM Pattern**: ViewModel ile state management
- **Jetpack Compose**: Modern, declarative UI
- **Hilt**: Dependency injection
- **Kotlin Coroutines + Flow**: Asenkron işlemler
- **Navigation Compose**: Ekranlar arası geçiş
- **DataStore**: Kullanıcı tercihleri
- **Coil**: Image loading

## 🏗️ Proje Yapısı

```
app/src/main/java/com/photoai/editor/
├── data/                      # Data layer
│   ├── ads/                   # AdMob yönetimi
│   ├── billing/               # Google Play Billing
│   ├── local/                 # Local data sources (DataStore)
│   └── repository/            # Repository implementations
├── domain/                    # Domain layer
│   ├── model/                 # Domain modelleri
│   ├── repository/            # Repository interfaces
│   └── usecase/               # Use cases
├── presentation/              # ViewModels
│   ├── splash/
│   ├── onboarding/
│   ├── home/
│   ├── edit/
│   └── premium/
├── ui/                        # UI layer
│   ├── components/            # Reusable components
│   ├── navigation/            # Navigation setup
│   ├── screens/               # Screen composables
│   └── theme/                 # App theme
├── di/                        # Dependency injection modules
└── utils/                     # Utility classes
```

## 🚀 Başlangıç

### Gereksinimler
- Android Studio Hedgehog (2023.1.1) veya üzeri
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Kotlin 1.9.20
- Gradle 8.2

### Kurulum

1. **Projeyi klonlayın**
```bash
git clone https://github.com/yourusername/photoai-editor.git
cd photoai-editor
```

2. **AdMob Ayarları**
   - `app/build.gradle.kts` dosyasında `admobAppId` değerini kendi AdMob App ID'niz ile değiştirin
   - `data/ads/AdManager.kt` dosyasında test ad unit ID'lerini gerçek ID'lerinizle değiştirin:
     ```kotlin
     const val BANNER_AD_UNIT_ID = "ca-app-pub-XXXXX/YYYY"
     const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-XXXXX/YYYY"
     ```

3. **Google Play Billing Ayarları**
   - Google Play Console'da subscription ürünü oluşturun
   - `data/billing/BillingManager.kt` dosyasında product ID'yi güncelleyin:
     ```kotlin
     private const val PREMIUM_MONTHLY_PRODUCT_ID = "your_product_id"
     ```

4. **Projeyi Sync Edin**
   - Android Studio'da projeyi açın
   - Gradle sync işlemini bekleyin

5. **Çalıştırın**
   - Emulator veya fiziksel cihazda uygulamayı çalıştırın

## 🔧 Yapılandırma

### AI Filtre Entegrasyonu

Şu anda AI filtreleri mock implementasyon ile çalışıyor. Gerçek AI API entegre etmek için:

1. **API Entegrasyonu**
   - `data/repository/AiImageProcessingRepositoryImpl.kt` dosyasını güncelleyin
   - Retrofit ile AI API endpoint'lerinizi tanımlayın
   - `applyFilter` metodunu gerçek API çağrısı yapacak şekilde değiştirin

2. **On-Device ML Model**
   - TensorFlow Lite kullanarak cihaz üzerinde çalışan model ekleyin
   - ML Kit veya TensorFlow Lite Interpreter kullanın

### Reklam Ayarları

**Test Reklamları**: Proje test ad ID'leri ile yapılandırılmış durumda.

**Production'a Geçiş**:
1. AdMob hesabınızdan ad unit'leri oluşturun
2. `AdManager.kt` dosyasındaki test ID'lerini gerçek ID'lerle değiştirin
3. Release build oluşturun

**Reklam Sıklığı**:
- Banner: Home ekranında sürekli gösterilir
- Interstitial: Her 3 export işleminde bir gösterilir
- Premium kullanıcılar hiç reklam görmez

### Premium Abonelik

**Test Etme**:
1. Google Play Console'da test ürünleri oluşturun
2. Test hesaplarınızı License Testing'e ekleyin
3. Internal testing track'i kullanarak test edin

**Production**:
1. Subscription ürününüzü oluşturun
2. Fiyatlandırma belirleyin
3. Product ID'yi `BillingManager.kt` dosyasında güncelleyin

## 📱 Ekranlar

### 1. Splash Screen
- Uygulama logosu ve animasyon
- Onboarding durumu kontrolü

### 2. Onboarding
- 3 sayfalık tanıtım ekranı
- Skip ve Next butonları
- Sadece ilk açılışta gösterilir

### 3. Home Screen
- Galeri seçimi
- Premium badge (premium kullanıcılar için)
- Banner reklam (free kullanıcılar için)

### 4. Edit Screen
- Fotoğraf görüntüleme
- Filtre seçimi (yatay scroll)
- Önce/sonra karşılaştırma (press and hold)
- Kaydet ve paylaş butonları
- Premium kilitli filtreler

### 5. Premium Screen
- Premium özellikleri listesi
- Fiyatlandırma
- Satın alma butonu
- Satın almayı geri yükle

## 🎨 Tema ve Tasarım

- **Renk Paleti**: Mor/Mavi gradient teması
- **Dark/Light Mode**: Sistem ayarına göre otomatik
- **Material Design 3**: Modern, consistent UI
- **Dynamic Colors**: Android 12+ için dynamic color desteği

## 🧪 Test

### Unit Test
```bash
./gradlew test
```

Temel use case'ler için unit testler:
- `domain/usecase/` altındaki test dosyaları

### UI Test
```bash
./gradlew connectedAndroidTest
```

## 📦 Build

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

**Not**: Release build için keystore dosyanızı yapılandırmanız gerekir.

## 🔐 Güvenlik

- **ProGuard/R8**: Release build'de kod obfuscation aktif
- **API Keys**: Hassas anahtarlar local.properties'de saklanmalı
- **Billing Security**: Satın almaları backend'de doğrulamayı düşünün

## 📝 TODO'lar

### Öncelikli
- [ ] Gerçek AI API entegrasyonu
- [ ] Backend purchase verification
- [ ] Kamera özelliği
- [ ] Paylaşma özelliği implementasyonu
- [ ] Analytics entegrasyonu (Firebase Analytics)

### Gelecek Özellikler
- [ ] Sosyal medya paylaşımı
- [ ] Fotoğraf koleksiyonları
- [ ] Batch processing
- [ ] Custom filtreler
- [ ] Video editing desteği

## 📄 Lisans

Bu proje [MIT Lisansı](LICENSE) altında lisanslanmıştır.

## 🤝 Katkıda Bulunma

1. Fork edin
2. Feature branch oluşturun (`git checkout -b feature/AmazingFeature`)
3. Commit edin (`git commit -m 'Add some AmazingFeature'`)
4. Push edin (`git push origin feature/AmazingFeature`)
5. Pull Request açın

## 📞 İletişim

Sorularınız için issue açabilir veya pull request gönderebilirsiniz.

## 🙏 Teşekkürler

- Jetpack Compose team
- Android team
- Open source community

---

**Not**: Bu uygulama production-ready bir iskelet olarak tasarlanmıştır. Gerçek AI API'leri, ödeme doğrulama sistemleri ve güvenlik önlemleri ekleyerek kullanıma hazır hale getirebilirsiniz.
