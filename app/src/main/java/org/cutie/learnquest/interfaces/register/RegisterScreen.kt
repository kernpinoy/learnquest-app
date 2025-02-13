package org.cutie.learnquest.interfaces.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cutie.learnquest.models.RegisterForm

@Composable
fun RegisterScreen(
    registerForm: RegisterForm,
    validationErrors: List<String>,
    isLoading: Boolean,
    onGoBackClicked: () -> Unit,
    onFormFieldChange: (RegisterForm) -> Unit,
    onRegisterClick: () -> Unit
) {
    var isGenderDropdownExpanded by remember { mutableStateOf(false) }
    var isDialogVisible by remember { mutableStateOf(false) }

    // Get screen width for responsive design
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xffffb344))
            .verticalScroll(rememberScrollState()), // Make the content scrollable
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = if (screenWidth < 400.dp) 20.sp else 24.sp // Adjust font size
            ),
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp)) // Reduced spacing

        // Text Fields with constrained width
        RegisterTextField(
            value = registerForm.lrn,
            onValueChange = { onFormFieldChange(registerForm.copy(lrn = it)) },
            label = "Learners Reference Number (12 digits)",
            modifier = Modifier
                .fillMaxWidth(0.9f) // Use 90% of screen width
                .padding(vertical = 4.dp) // Reduced padding
        )

        RegisterTextField(
            value = registerForm.firstName,
            onValueChange = { onFormFieldChange(registerForm.copy(firstName = it)) },
            label = "First Name",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 4.dp)
        )

        RegisterTextField(
            value = registerForm.middleName,
            onValueChange = { onFormFieldChange(registerForm.copy(middleName = it)) },
            label = "Middle Name",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 4.dp)
        )

        RegisterTextField(
            value = registerForm.lastName,
            onValueChange = { onFormFieldChange(registerForm.copy(lastName = it)) },
            label = "Last Name",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 4.dp)
        )

        GenderDropdown(
            selectedGender = registerForm.sex,
            isExpanded = isGenderDropdownExpanded,
            onExpandedChange = { isGenderDropdownExpanded = it },
            onGenderSelected = { gender -> onFormFieldChange(registerForm.copy(sex = gender)) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 4.dp)
        )

        PasswordTextField(
            value = registerForm.password,
            onValueChange = { onFormFieldChange(registerForm.copy(password = it)) },
            isVisible = registerForm.isPasswordVisible,
            onVisibilityChange = { onFormFieldChange(registerForm.copy(isPasswordVisible = it)) },
            label = "Password",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 4.dp)
        )

        PasswordTextField(
            value = registerForm.confirmPassword,
            onValueChange = { onFormFieldChange(registerForm.copy(confirmPassword = it)) },
            isVisible = registerForm.isPasswordVisible,
            onVisibilityChange = { onFormFieldChange(registerForm.copy(isPasswordVisible = it)) },
            label = "Confirm Password",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 4.dp)
        )

        RegisterTextField(
            value = registerForm.classCode,
            onValueChange = { onFormFieldChange(registerForm.copy(classCode = it)) },
            label = "Class Code",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 4.dp)
        )

        TermsAndConditionsCheckbox(
            checked = registerForm.isTermsConditionChecked,
            onCheckedChange = { isChecked -> onFormFieldChange(registerForm.copy(isTermsConditionChecked = isChecked)) },
            onTermsClicked = { isDialogVisible = true },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Reduced spacing

        RegisterButton(
            enabled = !isLoading && registerForm.hasRequiredFields() && registerForm.isTermsConditionChecked,
            onClick = onRegisterClick,
            isLoading = isLoading,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        GoBackButton(
            onClick = onGoBackClicked,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 4.dp)
        )
    }

    if (isDialogVisible) {
        TermsAndConditionsDialog(
            onDismissRequest = { isDialogVisible = false },
            onAgree = { onFormFieldChange(registerForm.copy(isTermsConditionChecked = true)) }
        )
    }
}

@Composable
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        modifier = modifier
    )
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    label: String,
    onVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { onVisibilityChange(!isVisible) }) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isVisible) "Hide password" else "Show password"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderDropdown(
    selectedGender: String,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onGenderSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val genders = listOf("Male", "Female")

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        TextField(
            value = if (selectedGender.isEmpty()) "Sex" else selectedGender,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(gender) },
                    onClick = {
                        onGenderSelected(gender)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
private fun RegisterButton(
    enabled: Boolean,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text("Create Account")
        }
    }
}

@Composable
private fun GoBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
    ) {
        Text(text = "Go Back", color = Color.Black)
    }
}

@Composable
private fun TermsAndConditionsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTermsClicked() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "I agree to the Terms and Conditions",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TermsAndConditionsDialog(
    onDismissRequest: () -> Unit,
    onAgree: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text("LearnQuest Terms and Conditions")
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = """
                        Welcome to LearnQuest! Please read these Terms and Conditions carefully before using our app. 
                        Through downloading and using LearnQuest, you agree to comply with and be bound by the following terms.
                        
                        1. Usage Rights
                        LearnQuest is an educational tool designed for kindergarten students at Paaralang Elementarya ng Lucban II. The app is intended solely for academic purposes. Any unauthorized commercial use of the app or its content is strictly prohibited.

                        2. Intellectual Property
                        All content within LearnQuest, including but not limited to 3D models, animations, and educational modules, is the intellectual property of the creators. These materials are protected by copyright laws and may not be reproduced, distributed, or transmitted in any form without prior written consent from the authors.

                        The stories included in the app are in the public domain, meaning they are free for use by anyone. However, the recordings of these stories, as well as any adaptations made by the developers, are the intellectual property of the creators. These recordings and adaptations may not be reproduced, distributed, or transmitted in any form without prior written consent from the authors.

                        3. Distribution of Learning Materials
                        The educational materials provided within LearnQuest are strictly for academic use. You may not distribute, modify, transmit, reuse, download, repost, copy, or use the content of these modules for commercial purposes or public distribution without explicit written permission from the authors.

                        4. Location and Governing Law
                        LearnQuest is developed and maintained by its creators based in Lucban, Quezon, Philippines. All interactions, transactions, and legal matters related to this app shall be governed by the laws applicable in this jurisdiction.

                        5. User Responsibilities
                        As a user, you are responsible for ensuring that your use of LearnQuest complies with these Terms and Conditions. Any misuse of the app may result in suspension or termination of your access to the platform.

                        6. Updates and Modifications
                        We reserve the right to update or modify these Terms and Conditions at any time without prior notice. Your continued use of LearnQuest after any changes indicates your acceptance of the revised terms.

                        7. Contact Information
                        If you have any questions or need further clarification about these Terms and Conditions, please contact us at learnquest@gmail.com.

                        By using LearnQuest, you acknowledge that you have read, understood, and agree to be bound by these Terms and Conditions. Thank you for choosing LearnQuest to enhance your learning experience!
                    """.trimIndent()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onAgree()
                    onDismissRequest()
                }
            ) {
                Text("I Agree")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    val registerForm = RegisterForm(
        lrn = "",
        firstName = "",
        middleName = "",
        lastName = "",
        sex = "",
        password = "",
        confirmPassword = "",
        classCode = "",
        isPasswordVisible = false,
        isTermsConditionChecked = false
    )

    RegisterScreen(
        registerForm = registerForm,
        validationErrors = listOf(),
        isLoading = false,
        onGoBackClicked = {},
        onFormFieldChange = {},
        onRegisterClick = {}
    )
}