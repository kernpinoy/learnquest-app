package org.cutie.learnquest.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cutie.learnquest.components.MainMenuButton

@Composable
fun ActivitiesScreen(
    onBackClicked: () -> Unit,
    onShapesClicked: () -> Unit,
    onLettersClicked: () -> Unit,
    onNumberClicked: () -> Unit,
    onColorClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFD780))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = "Activities",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.wrapContentSize(Alignment.Center),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Centered Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MainMenuButton("Letters", onLettersClicked)
            MainMenuButton("Shapes", onShapesClicked)
            MainMenuButton("Colors", onColorClicked)
            MainMenuButton("Numbers", onNumberClicked)
        }

        Spacer(modifier = Modifier.weight(1f)) // Push the logout button to the bottom

        // Centered Back Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onBackClicked,
                modifier = Modifier
                    .fillMaxWidth(0.5f) // Make it smaller than full width
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(text = "Go back", color = Color.Black)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivitiesScreenPreview() {
    ActivitiesScreen(
        onBackClicked = {},
        onShapesClicked = {},
        onLettersClicked = {},
        onNumberClicked = {},
        onColorClicked = {}
    )
}