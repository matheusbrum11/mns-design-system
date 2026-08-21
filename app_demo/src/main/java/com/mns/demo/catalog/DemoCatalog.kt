package com.mns.demo.catalog

import com.mns.demo.catalog.entries.actionEntries
import com.mns.demo.catalog.entries.collectionEntries
import com.mns.demo.catalog.entries.inputEntries
import com.mns.demo.catalog.entries.layoutEntries
import com.mns.demo.catalog.entries.loadingAndCodeEntries
import com.mns.demo.catalog.entries.statusEntries
import com.mns.demo.catalog.entries.textEntries

/**
 * Registro único de tudo que o `app_demo` exibe.
 *
 * Serve também de rede de segurança: o teste de integração
 * `DemoCatalogCoverageTest` percorre este catálogo e falha se algum componente
 * público do design system não estiver representado aqui. Componente sem
 * demonstração é componente que ninguém descobre — e que ninguém testa.
 */
public object DemoCatalog {

    /** Todos os componentes do catálogo, na ordem de registro. */
    public val components: List<DemoComponent> by lazy {
        buildList {
            addAll(actionEntries())
            addAll(inputEntries())
            addAll(textEntries())
            addAll(statusEntries())
            addAll(layoutEntries())
            addAll(collectionEntries())
            addAll(loadingAndCodeEntries())
        }.also { entries ->
            val duplicados = entries.groupBy { it.id }.filterValues { it.size > 1 }.keys
            check(duplicados.isEmpty()) { "Ids duplicados no catálogo: $duplicados" }
        }
    }

    /** Categorias que possuem ao menos um componente, na ordem do enum. */
    public val categories: List<DemoCategory> by lazy {
        DemoCategory.entries.filter { category -> components.any { it.category == category } }
    }

    /** Componentes de uma categoria. */
    public fun byCategory(category: DemoCategory): List<DemoComponent> =
        components.filter { it.category == category }

    /** Busca por id; `null` se não existir. */
    public fun byId(id: String): DemoComponent? = components.firstOrNull { it.id == id }

    /** Busca textual simples por nome, resumo e categoria. */
    public fun search(query: String): List<DemoComponent> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return components
        return components.filter {
            q in it.name.lowercase() ||
                q in it.summary.lowercase() ||
                q in it.category.label.lowercase() ||
                q in it.id
        }
    }
}
