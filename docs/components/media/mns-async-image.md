# MnsAsyncImage

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsAsyncImage`

Imagem remota com carregamento assíncrono.

É o único ponto do design system que conhece a biblioteca de carregamento
(Coil) — do mesmo jeito que `MnsQrEncoder` é o único que conhece o ZXing.
Todos os componentes que aceitam uma URL (`MnsIcon`, `MnsAvatar`, `MnsCover`,
`MnsListLeading.RemoteThumbnail`) passam por aqui, então trocar de motor de
imagem no futuro não toca em nenhum componente.

O estado de carregamento usa o **shimmer do próprio design system**, não um
spinner genérico: a transição de placeholder para imagem fica coerente com o
resto dos esqueletos do app.

```kotlin
MnsAsyncImage(
    model = evento.capaUrl,
    contentDescription = "Capa de ${evento.nome}",
    modifier = Modifier.fillMaxWidth().height(180.dp),
)
```

**Configuração:** o `ImageLoader` usado é o singleton do Coil. Para definir
cache, cabeçalhos ou autenticação, implemente `ImageLoaderFactory` na sua
`Application` — ver `docs/components/media/mns-async-image.md`.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `model` | `Any?` | — | o que carregar: `String` de URL, `Uri`, `File`, id de drawable ou um `ImageRequest` do Coil já montado. |
| `contentDescription` | `String?` | — | descrição para leitores de tela. `null` marca a imagem como decorativa. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `shape` | `Shape` | `MnsTheme.shapes.image` | recorte aplicado à imagem e ao placeholder. |
| `contentScale` | `ContentScale` | `ContentScale.Crop` | como a imagem preenche a área. |
| `colorFilter` | `ColorFilter?` | `null` | filtro de cor; use para tingir arte monocromática. |
| `showShimmerWhileLoading` | `Boolean` | `true` | exibe o shimmer enquanto carrega. Desligue em listas muito densas, onde N shimmers simultâneos competem por atenção. |
| `fallback` | `(@Composable BoxScope.() -> Unit)?` | `null` | conteúdo exibido quando a carga falha. `null` deixa a área com a cor de superfície — sem ícone de erro, porque em card de conteúdo um ícone quebrado costuma incomodar mais que um espaço vazio. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/media/MnsAsyncImage.kt`
