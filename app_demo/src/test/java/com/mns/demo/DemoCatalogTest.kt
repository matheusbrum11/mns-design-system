package com.mns.demo

import com.google.common.truth.Truth.assertThat
import com.mns.demo.catalog.DemoCatalog
import com.mns.demo.catalog.DemoCategory
import org.junit.Test
import java.io.File

/**
 * Testes do catálogo.
 *
 * O catálogo é o contrato entre a biblioteca e a sua documentação viva: se um
 * componente existe mas não está aqui, ele não tem demonstração, não tem
 * documento apontado e ninguém descobre que ele existe. Estes testes tornam
 * isso um erro de build em vez de uma descoberta tardia.
 */
class DemoCatalogTest {

    @Test
    fun `ids do catalogo sao unicos`() {
        val ids = DemoCatalog.components.map { it.id }
        assertThat(ids).containsNoDuplicates()
    }

    @Test
    fun `catalogo cobre todas as categorias declaradas`() {
        assertThat(DemoCatalog.categories).containsExactlyElementsIn(DemoCategory.entries)
    }

    @Test
    fun `toda entrada tem nome resumo e caminho de documentacao`() {
        DemoCatalog.components.forEach { componente ->
            assertThat(componente.name).isNotEmpty()
            assertThat(componente.summary).isNotEmpty()
            assertThat(componente.docPath).startsWith("docs/")
            assertThat(componente.docPath).endsWith(".md")
        }
    }

    @Test
    fun `toda entrada aponta para um documento que existe no repositorio`() {
        val raiz = File(System.getProperty("user.dir")!!).parentFile
        val ausentes = DemoCatalog.components
            .map { it.docPath }
            .distinct()
            .filter { !File(raiz, it).exists() }
        assertThat(ausentes).isEmpty()
    }

    @Test
    fun `entradas com parametros expoem knobs com chave unica`() {
        DemoCatalog.components.forEach { componente ->
            val chaves = componente.knobs.map { it.key }
            assertThat(chaves).containsNoDuplicates()
            componente.knobs.forEach { knob ->
                assertThat(knob.label).isNotEmpty()
            }
        }
    }

    @Test
    fun `busca por id nome resumo e categoria encontra a entrada`() {
        val alvo = DemoCatalog.byId("mns-button")
        assertThat(alvo).isNotNull()
        assertThat(DemoCatalog.search("mns-button")).contains(alvo)
        assertThat(DemoCatalog.search("MnsButton")).contains(alvo)
        assertThat(DemoCatalog.search("ênfase").isEmpty()).isFalse()
        assertThat(DemoCatalog.search("Ações")).contains(alvo)
    }

    @Test
    fun `busca vazia devolve o catalogo inteiro`() {
        assertThat(DemoCatalog.search("   ")).hasSize(DemoCatalog.components.size)
    }

    @Test
    fun `busca sem correspondencia devolve lista vazia`() {
        assertThat(DemoCatalog.search("componente-que-nao-existe")).isEmpty()
    }

    @Test
    fun `byId devolve nulo para id desconhecido`() {
        assertThat(DemoCatalog.byId("nao-existe")).isNull()
    }

    @Test
    fun `byCategory devolve apenas entradas daquela categoria`() {
        DemoCategory.entries.forEach { categoria ->
            DemoCatalog.byCategory(categoria).forEach {
                assertThat(it.category).isEqualTo(categoria)
            }
        }
    }
}
