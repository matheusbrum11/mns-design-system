# Consumer ProGuard rules do MNS Design System.
# Mantem os nomes dos tokens serializaveis usados pelo Design Contract.
-keepattributes *Annotation*, InnerClasses
-keepclassmembers class com.mns.designsystem.contract.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.mns.designsystem.contract.**$$serializer { *; }
-keep class com.mns.designsystem.contract.** { *; }
