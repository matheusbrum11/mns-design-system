# Proteção da `main`

[← Documentação](README.md) · [CI/CD](ci-cd.md) · [Contribuição](../CONTRIBUTING.md)

A `main` é protegida por um **ruleset**. Ele mora nas **configurações do
repositório** — *Settings → Rules → Rulesets* — e o JSON versionado em
[`docs/rulesets/`](rulesets/README.md) é a **fonte** dele, não a configuração em
si: o GitHub não lê arquivo nenhum do repositório para montar rulesets.

Consequência prática: **editar o JSON e mergear não muda proteção nenhuma.**
Toda alteração precisa ser reimportada. O que ele garante:

| Regra | Efeito |
|---|---|
| `pull_request` | **Nenhum push direto.** Todo código entra por PR — inclusive o do dono do repositório. |
| `required_status_checks` | **Sem CI verde não há merge.** O check exigido é `Lint · Testes · Cobertura`. |
| `required_approving_review_count` | No ruleset completo, merge exige aprovação de outra pessoa com acesso de escrita. |
| `require_code_owner_review` | No ruleset completo, a aprovação precisa vir de quem está no [`CODEOWNERS`](../.github/CODEOWNERS). |
| `non_fast_forward` | Sem `push --force`: história reescrita apaga rastro de auditoria. |
| `deletion` | A `main` não pode ser apagada. |
| `bypass_actors: []` | **Vazio de propósito.** Ninguém contorna, nem administrador. É a diferença entre uma regra e uma sugestão. |

Complementos que valem conhecer:

- `strict_required_status_checks_policy` — o PR precisa estar atualizado com a
  `main` antes do merge. Evita o caso em que dois PRs passam sozinhos e quebram
  juntos.
- `dismiss_stale_reviews_on_push` — um push novo derruba as aprovações. Sem
  isso, aprova-se um diff e mergeia-se outro.
- `require_last_push_approval` — quem fez o último push não pode ser o aprovador.
- `required_review_thread_resolution` — comentários abertos travam o merge.

---

## Como aplicar

### Pela interface

**`Settings` → `Rules` → `Rulesets` → `New ruleset` → `Import a ruleset`**

Link direto:
`https://github.com/matheusbrum11/mns-design-system/settings/rules`

Selecione o arquivo `docs/rulesets/protecao-main.json` e confirme.

### Pela API

```bash
export GH_TOKEN=...   # token com permissão Administration: read and write

curl -X POST \
  -u "${GH_TOKEN}:x-oauth-basic" \
  -H "Accept: application/vnd.github+json" \
  -H "Content-Type: application/json" \
  https://api.github.com/repos/matheusbrum11/mns-design-system/rulesets \
  -d @docs/rulesets/protecao-main.json
```

Para atualizar um ruleset existente, troque por `PUT .../rulesets/{id}` — o `id`
sai de `GET .../rulesets`.

---

## Qual dos dois arquivos usar

| Arquivo | Exige revisor | Use quando |
|---|---|---|
| [`protecao-main.json`](rulesets/protecao-main.json) | sim | Há **pelo menos duas pessoas** com acesso de escrita |
| [`protecao-main-sem-revisor.json`](rulesets/protecao-main-sem-revisor.json) | não | O projeto ainda é de uma pessoa |

> **Ninguém aprova o próprio PR no GitHub.** Num repositório de uma pessoa só,
> exigir uma aprovação trava *todos* os seus PRs: não há quem aprove, e como
> `bypass_actors` está vazio, nem o dono contorna. O resultado é uma `main` na
> qual não se consegue mais mergear nada.
>
> Por isso os dois arquivos. O sem revisor mantém as duas garantias que
> funcionam sozinho — **CI obrigatório** e **nenhum push direto** — e você troca
> pelo completo assim que houver um segundo revisor.

---

## Sobre "revisor com acesso de admin ou engineer"

Este repositório pertence a uma **conta de usuário**, não a uma organização.
Isso impõe dois limites reais:

1. **Não existem *teams*.** O papel "engineer" não é modelável; o `CODEOWNERS`
   precisa listar pessoas, uma a uma.
2. **Rulesets não filtram aprovação por papel.** Não há como dizer "só vale
   aprovação de quem é admin". O mecanismo equivalente é o `CODEOWNERS`: a
   aprovação tem de vir de quem está listado nele, e você controla essa lista.

Os papéis disponíveis para colaboradores num repositório pessoal são *Read*,
*Triage*, *Write*, *Maintain* e *Admin* — configurados em
**Settings → Collaborators**. Aprovação de quem tem apenas *Read* **não conta**
para o requisito de revisão.

Se o projeto migrar para uma organização, o `CODEOWNERS` passa a aceitar
`@minha-org/engenharia` e aí sim o papel vira o critério, sem manutenção de
lista.

---

## O JSON e a configuração podem divergir

Nada impede alguém de mexer no ruleset pela interface sem tocar no arquivo — aí
o repositório passa a documentar uma proteção que não é a real, que é pior do
que não documentar nada.

Hoje isso é disciplina, não garantia: **reimporte a cada alteração**. Se o
projeto crescer, o passo seguinte é um workflow que compare `GET /rulesets` com
o JSON versionado e falhe na divergência — precisa de um token com
`Administration: read`, guardado como secret.

## Conferindo que pegou

Depois de aplicar, tente empurrar direto na `main`:

```bash
git switch main && git commit --allow-empty -m "teste" && git push
```

O push deve ser recusado com `protected branch hook declined` e a menção à regra
que barrou. Desfaça com `git reset --hard origin/main`.
