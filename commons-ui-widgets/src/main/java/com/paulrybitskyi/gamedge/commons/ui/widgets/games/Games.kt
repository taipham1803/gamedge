/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulrybitskyi.gamedge.commons.ui.widgets.games

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.AnimatedContentContainer
import com.paulrybitskyi.gamedge.commons.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.RefreshableContent

@Composable
fun Games(
    uiState: GamesUiState,
    onGameClicked: (GameModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    AnimatedContentContainer(uiState.finiteUiState) { finiteUiState ->
        when (finiteUiState) {
            FiniteUiState.EMPTY -> GamesEmptyState(
                uiState = uiState,
                modifier = Modifier.align(Alignment.Center)
            )
            FiniteUiState.LOADING -> GamesLoadingState(Modifier.align(Alignment.Center))
            FiniteUiState.SUCCESS -> GamesSuccessState(
                uiState = uiState,
                modifier = Modifier.matchParentSize(),
                onGameClicked = onGameClicked,
                onBottomReached = onBottomReached,
            )
        }
    }
}

@Composable
private fun GamesLoadingState(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = GamedgeTheme.colors.secondary,
    )
}

@Composable
private fun GamesEmptyState(
    uiState: GamesUiState,
    modifier: Modifier,
) {
    Info(
        icon = painterResource(uiState.infoIconId),
        title = uiState.infoTitle,
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.games_info_view_horizontal_padding)
        ),
    )
}

@Composable
private fun GamesSuccessState(
    uiState: GamesUiState,
    modifier: Modifier,
    onGameClicked: (GameModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    RefreshableContent(
        isRefreshing = uiState.isRefreshing,
        modifier = modifier,
        isSwipeEnabled = false,
    ) {
        val games = uiState.games
        val lastIndex = games.lastIndex

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.games_vertical_arrangement_spacing)
            )
        ) {
            itemsIndexed(
                items = games,
                key = { _, game -> game.id }
            ) { index, game ->
                if (index == lastIndex) {
                    LaunchedEffect(lastIndex) {
                        onBottomReached()
                    }
                }

                Game(
                    coverImageUrl = game.coverImageUrl,
                    name = game.name,
                    releaseDate = game.releaseDate,
                    developerName = game.developerName,
                    description = game.description,
                    onClick = { onGameClicked(game) },
                )
            }
        }
    }
}

@Preview
@Composable
internal fun GamesSuccessStatePreview() {
    val games = listOf(
        GameModel(
            id = 1,
            coverImageUrl = null,
            name = "Ghost of Tsushima: Director's Cut",
            releaseDate = "Aug 20, 2021 (2 months ago)",
            developerName = "Sucker Punch Productions",
            description = "Some very very very very very very very very very long description",
        ),
        GameModel(
            id = 2,
            coverImageUrl = null,
            name = "Forza Horizon 5",
            releaseDate = "Nov 09, 2021 (8 days ago)",
            developerName = "Playground Games",
            description = "Some very very very very very very very very very long description",
        ),
        GameModel(
            id = 3,
            coverImageUrl = null,
            name = "Outer Wilds: Echoes of the Eye",
            releaseDate = "Sep 28, 2021 (a month ago)",
            developerName = "Mobius Digital",
            description = "Some very very very very very very very very very long description",
        )
    )

    GamedgeTheme {
        Games(
            uiState = GamesUiState(
                isLoading = false,
                infoIconId = 0,
                infoTitle = "",
                games = games,
            ),
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Composable
internal fun GamesEmptyStatePreview() {
    GamedgeTheme {
        Games(
            uiState = GamesUiState(
                isLoading = false,
                infoIconId = R.drawable.gamepad_variant_outline,
                infoTitle = "No Games\nNo Games",
                games = emptyList(),
            ),
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Composable
internal fun GamesLoadingStatePreview() {
    GamedgeTheme {
        Games(
            uiState = GamesUiState(
                isLoading = true,
                infoIconId = 0,
                infoTitle = "",
                games = emptyList(),
            ),
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}
