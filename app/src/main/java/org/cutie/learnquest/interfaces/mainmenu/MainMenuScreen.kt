package org.cutie.learnquest.interfaces.mainmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun MainMenuScreen(
    onArClicked: () -> Unit,
    onLearningMaterialsClicked: () -> Unit,
    onActivitiesClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFD780)) // Match the orange background color
            .padding(16.dp)
    ) {
        // Title at the top
        Text(
            text = "Main Menu",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start), // Align title to the start (left)
            color = Color.Black
        )

        // Spacer to add space between title and buttons
        Spacer(modifier = Modifier.height(40.dp))

        // Centered buttons
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f), // Ensure the buttons take the available space in the center
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center buttons vertically
        ) {
            MainMenuButton("Augmented Reality", onArClicked)
            MainMenuButton("Learning Modules", onLearningMaterialsClicked)
            MainMenuButton("Activities", onActivitiesClicked)

            // Spacer to add some space before the logout button
            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = onLogoutClicked,
                modifier = Modifier
                    .fillMaxWidth(0.5f) // Make it smaller than full width
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(text = "Logout", color = Color.Black)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    MainMenuScreen(
        onLogoutClicked = {},
        onArClicked = {},
        onActivitiesClicked = {},
        onLearningMaterialsClicked = {}
    )
}

