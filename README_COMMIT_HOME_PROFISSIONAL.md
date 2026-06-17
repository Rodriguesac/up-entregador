# Pacote commitável — Home Profissional Rodrigues Entregador

Este pacote é a versão corrigida para commitar no repositório `up-entregador`.

Correção principal:

```kotlin
import androidx.compose.foundation.layout.Column
```

Também inclui a função `RodriguesProfessionalTheme`, caso o app chame a Home dentro desse tema.

## Aplicar e commitar no Termux

```bash
cd /data/data/com.termux/files/home/up-entregador || exit

unzip -o /sdcard/Download/rodrigues_professional_home_compose_commitavel_v2.zip

git status

git add app/src/main/java/com/rodriguesacai/entregador/ui/professional
git add README_INSTALACAO.md README_COMMIT_HOME_PROFISSIONAL.md

git commit -m "fix: corrige pacote da home profissional"
git push
```

## Conferir a correção antes do commit

```bash
grep -n "foundation.layout.Column" app/src/main/java/com/rodriguesacai/entregador/ui/professional/ProfessionalHomeScreen.kt
```

Se aparecer uma linha com esse import, a falha `Unresolved reference 'Column'` foi corrigida.
