package io.searching.server.api.searchranking.adapter.inp.web

import io.searching.server.api.searchranking.adapter.inp.service.KeywordCounter
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
internal class SearchRankingApiTest @Autowired constructor(
    private val keywordCounter: KeywordCounter
) {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `검색어 랭킹 조회`() {
        keywordCounter.initialize()

        (1..2).forEach {
            keywordCounter.increase("keyword$it", it)
        }

        mockMvc.perform(get("/search-rankings"))
            .andExpectAll(
                status().isOk,
                jsonPath("$.searchRankings[0].keyword").value("keyword2"),
                jsonPath("$.searchRankings[0].count").value(2),
                jsonPath("$.searchRankings[1].keyword").value("keyword1"),
                jsonPath("$.searchRankings[1].count").value(1)
            )
    }
}
