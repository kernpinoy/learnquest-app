package org.cutie.learnquest.models

data class LoginForm(
    var username: String = "", var password: String = ""
) {
    fun isValid(): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}

data class RegisterForm(
    val lrn: String = "",
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val sex: String = "",
    val password: String = "",
    val classCode: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isTermsConditionChecked: Boolean = false
) {
    companion object {
        const val MIN_PASSWORD_LENGTH = 8
        const val LRN_LENGTH = 12
        val VALID_SEX_OPTIONS = listOf("Male", "Female", "Other")
    }

    sealed class ValidationResult {
        object Valid : ValidationResult()
        data class Invalid(val errors: List<ValidationError>) : ValidationResult()
    }

    sealed class ValidationError {
        object EmptyLRN : ValidationError()
        object InvalidLRNLength : ValidationError()
        object EmptyFirstName : ValidationError()
        object EmptyMiddleName : ValidationError()
        object EmptyLastName : ValidationError()
        object EmptySex : ValidationError()
        object InvalidSexOption : ValidationError()
        object EmptyPassword : ValidationError()
        object PasswordTooShort : ValidationError()
        object PasswordsDoNotMatch : ValidationError()
        object EmptyClassCode : ValidationError()
        object TermsNotAccepted : ValidationError()

        fun getMessage(): String = when (this) {
            is EmptyLRN -> "LRN is required"
            is InvalidLRNLength -> "LRN must be exactly $LRN_LENGTH digits"
            is EmptyFirstName -> "First name is required"
            is EmptyMiddleName -> "Middle name is required"
            is EmptyLastName -> "Last name is required"
            is EmptySex -> "Sex is required"
            is InvalidSexOption -> "Please select a valid option for sex"
            is EmptyPassword -> "Password is required"
            is PasswordTooShort -> "Password must be at least $MIN_PASSWORD_LENGTH characters"
            is PasswordsDoNotMatch -> "Passwords do not match"
            is EmptyClassCode -> "Class code is required"
            is TermsNotAccepted -> "You must accept the terms and conditions"
        }
    }

    fun validate(): ValidationResult {
        val errors = mutableListOf<ValidationError>()

        // LRN validation
        when {
            lrn.isEmpty() -> errors.add(ValidationError.EmptyLRN)
            lrn.length != LRN_LENGTH -> errors.add(ValidationError.InvalidLRNLength)
        }

        // Name validation
        if (firstName.isEmpty()) errors.add(ValidationError.EmptyFirstName)
        if (middleName.isEmpty()) errors.add(ValidationError.EmptyMiddleName)
        if (lastName.isEmpty()) errors.add(ValidationError.EmptyLastName)

        // Sex validation
        when {
            sex.isEmpty() -> errors.add(ValidationError.EmptySex)
            sex !in VALID_SEX_OPTIONS -> errors.add(ValidationError.InvalidSexOption)
        }

        // Password validation
        when {
            password.isEmpty() -> errors.add(ValidationError.EmptyPassword)
            password.length < MIN_PASSWORD_LENGTH -> errors.add(ValidationError.PasswordTooShort)
            password != confirmPassword -> errors.add(ValidationError.PasswordsDoNotMatch)
        }

        // Class code validation
        if (classCode.isEmpty()) errors.add(ValidationError.EmptyClassCode)

        // Terms and conditions validation
        if (!isTermsConditionChecked) errors.add(ValidationError.TermsNotAccepted)

        return if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
    }

    fun isValid(): Boolean = validate() is ValidationResult.Valid

    fun hasRequiredFields(): Boolean {
        return lrn.isNotEmpty() &&
                firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                sex.isNotEmpty() &&
                password.isNotEmpty() &&
                confirmPassword.isNotEmpty() &&
                classCode.isNotEmpty() &&
                isTermsConditionChecked
    }

    fun toRegistrationRequest(): RegistrationRequest {
        return RegistrationRequest(
            lrn = lrn,
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            sex = sex,
            password = password,
            classCode = classCode
        )
    }
}

// Data class for API request
data class RegistrationRequest(
    val lrn: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val sex: String,
    val password: String,
    val classCode: String
)