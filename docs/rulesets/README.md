# Rulesets

[← Proteção da main](../protecao-da-main.md)

> **Estes arquivos não são lidos pelo GitHub.**
>
> Diferente de `.github/workflows/`, **não existe** convenção
> `.github/rulesets/`. Um ruleset vive nas **configurações do repositório**
> (*Settings → Rules → Rulesets*) e só é criado ou alterado pela interface ou
> pela API REST. Editar o JSON aqui e mergear **não muda proteção nenhuma**.
>
> Foi por isso que eles saíram de `.github/`: aquele diretório sugere que o
> GitHub o interpreta, e aqui isso seria mentira.

## Então por que versionar

Porque o formato é o mesmo que a UI exporta e importa, e ter o arquivo no
repositório dá três coisas que a configuração sozinha não dá:

| | |
|---|---|
| **Revisão** | Mudar quem pode mergear na `main` vira um diff, discutido em PR, e não um clique que ninguém vê |
| **Histórico** | `git log` responde quando a regra mudou e por quê |
| **Recuperação** | Ruleset apagado por engano volta com um import |

O que o arquivo **não** dá é garantia de que a configuração real bate com ele.
Isso exigiria uma verificação de divergência — ver
[Proteção da main](../protecao-da-main.md).

## Aplicando

**`Settings` → `Rules` → `Rulesets` → `New ruleset` → `Import a ruleset`**

Depois de qualquer alteração aqui, **reimporte**. O arquivo é a fonte; a
configuração é o que vale.
