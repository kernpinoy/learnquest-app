package org.cutie.learnquest.interfaces.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cutie.learnquest.R
import org.cutie.learnquest.models.LoginForm

// Custom colors
private val OrangeBackground = Color(0xFFFFAB40)
private val RedRegister = Color(0xFFFF5252)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginForm: LoginForm,
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
    onFormFieldChange: (String, String) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Welcome banner with exact orange color
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(OrangeBackground),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "WELCOME!",
                        fontSize = 50.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 30.dp)
                    )
                    // Train illustration using tren_design_.xml
                    Image(
                        painter = painterResource(id = R.drawable.tren_design_),
                        contentDescription = "Train Illustration",
                        modifier = Modifier
                            .size(250.dp) // Adjust size as needed
                    )
                }
            }

            // Login form
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = loginForm.username,
                    onValueChange = { onFormFieldChange(it, loginForm.password) },
                    label = { Text("LRN") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeBackground,
                        focusedLabelColor = OrangeBackground,
                        cursorColor = OrangeBackground
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = loginForm.password,
                    onValueChange = { onFormFieldChange(loginForm.username, it) },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeBackground,
                        focusedLabelColor = OrangeBackground,
                        cursorColor = OrangeBackground
                    ),
                    singleLine = true
                )

                Button(
                    onClick = onLoginClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeBackground
                    )
                ) {
                    Text("Login")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Don't have an account?",
                        color = Color.Gray
                    )
                    TextButton(onClick = onRegisterClicked) {
                        Text(
                            "Register",
                            color = RedRegister
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        loginForm = LoginForm(),
        onLoginClicked = {},
        onRegisterClicked = {},
        onFormFieldChange = { _, _ -> }
    )
}