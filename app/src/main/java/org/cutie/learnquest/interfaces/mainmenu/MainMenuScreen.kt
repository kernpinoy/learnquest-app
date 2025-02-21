package org.cutie.learnquest.interfaces.mainmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cutie.learnquest.components.MainMenuButton

@Composable
fun MainMenuScreen(
    isMusicPlaying: Boolean,
    onMusicToggle: () -> Unit,
    onArClicked: () -> Unit,
    onLearningMaterialsClicked: () -> Unit,
    onActivitiesClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFD780))
    ) {
        // Music control button in top-right corner
        IconButton(
            onClick = onMusicToggle,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color(0xFFEA6B6B),
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = if (isMusicPlaying) Icons.Filled.MusicNote else Icons.Filled.MusicOff,
                contentDescription = if (isMusicPlaying) "Turn off music" else "Turn on music",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Spacer to add space between title and buttons
            Spacer(modifier = Modifier.height(40.dp))

            // Centered buttons
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
                        .fillMaxWidth(0.5f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Logout", color = Color.Black)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    MainMenuScreen(
        isMusicPlaying = true,
        onMusicToggle = {},
        onLogoutClicked = {},
        onArClicked = {},
        onActivitiesClicked = {},
        onLearningMaterialsClicked = {}
    )
}