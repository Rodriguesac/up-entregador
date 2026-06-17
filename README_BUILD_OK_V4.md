# Rodrigues Entregador — Home Profissional build ok v4

Pacote corrigido para remover totalmente dependência direta de `Column` no `ProfessionalHomeScreen.kt`.

O arquivo usa `ProfessionalColumn`, uma coluna interna baseada em `Layout`, para evitar o erro recorrente:

```text
Unresolved reference Column
```

## Aplicar no Termux

```bash
cd /data/data/com.termux/files/home/up-entregador || exit

unzip -o /sdcard/Download/rodrigues_professional_home_build_ok_v4.zip

echo "Conferindo se sobrou Column original:"
grep -nE "(^|[^A-Za-z0-9_.])Column[[:space:]]*[({]|Column\." app/src/main/java/com/rodriguesacai/entregador/ui/professional/ProfessionalHomeScreen.kt || echo "OK: sem Column original"

git status
git add app/src/main/java/com/rodriguesacai/entregador/ui/professional
git add README_INSTALACAO.md README_BUILD_OK_V4.md aplicar_home_profissional_build_ok_v4.sh
git commit -m "fix: remove Column da home profissional"
git push origin HEAD:main
```

## Acompanhar build

```bash
sleep 8
RUN=$(gh run list -R Rodriguesac/up-entregador --branch main --limit 1 --json databaseId -q '.[0].databaseId')
echo "Build novo: $RUN"
gh run watch "$RUN" -R Rodriguesac/up-entregador --exit-status
```
