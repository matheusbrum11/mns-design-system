# tools

Ferramentas de apoio ao MNS Design System. Ambas são Python 3.9+ puro, sem
dependências externas — rodam em qualquer máquina e na CI sem instalar nada.

## `generate_component_docs.py`

Gera `docs/components/**` e `docs/tokens/**` a partir do KDoc do
`:design_system`.

```bash
python3 tools/generate_component_docs.py           # gera
python3 tools/generate_component_docs.py --check   # falha se estiver defasado
```

A fonte da verdade é o código: `@param` e `@property` viram a coluna
"Descrição"; a assinatura vira "Tipo" e "Padrão". Os resumos do índice são
lidos do catálogo do `:app_demo`, então a frase que aparece no app é a mesma que
aparece na documentação.

O modo `--check` roda na CI. Se alguém mexer em um parâmetro sem regerar, o
build falha listando os arquivos defasados.

**Ao adicionar um componente**, registre a página no dicionário `PAGES` do
script e rode o gerador.

## `check_docs_links.py`

Verifica que todo link relativo dos arquivos `.md` aponta para um arquivo que
existe. Links dentro de blocos de código são ignorados — ali são exemplo, não
navegação.

```bash
python3 tools/check_docs_links.py
```

Roda na CI. Um índice com link quebrado é pior que um índice ausente: ele
promete e não entrega.

## `sample_design_colors.py`

Amostra cores de um print de design. Decodifica PNG usando apenas `zlib` da
biblioteca padrão.

```bash
# cores dominantes — background e surface
python3 tools/sample_design_colors.py print.png --top 20

# cores saturadas — primary e accent, sem os cinzas atrapalhando
python3 tools/sample_design_colors.py print.png --saturated

# cor exata em um ponto
python3 tools/sample_design_colors.py print.png --at 356,635

# média de uma região (mais robusto que um pixel solto)
python3 tools/sample_design_colors.py print.png --region 340,620,380,650

# contraste WCAG entre duas cores
python3 tools/sample_design_colors.py --contrast "#FFFFFF" "#6255F4"
```

É a ferramenta que o agente [`mns-design-contract`](../.claude/agents/mns-design-contract.md)
usa para nunca estimar um hex "de olho". Ver
[Design Contract](../docs/design-contract.md).
