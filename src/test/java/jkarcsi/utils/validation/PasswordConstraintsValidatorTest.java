package jkarcsi.utils.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PasswordConstraintsValidatorTest {

    @Mock
    PasswordConstraintsValidator passwordConstraintsValidator;

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    public static final String WRONG_PASSWORDS_CSV = "/csv/WrongPasswords.csv";

    @BeforeEach
    public void setUp() {
        when(passwordConstraintsValidator.isValid(any(), any())).thenCallRealMethod();
        PasswordConstraintValidatorTestClass testClass = new PasswordConstraintValidatorTestClass();
        passwordConstraintsValidator.initialize(testClass);
    }

    @ParameterizedTest
    @CsvFileSource(resources = WRONG_PASSWORDS_CSV)
    void test_IsValidWithWeakPasswords_false(String input) {
        assertThat(passwordConstraintsValidator.isValid(input, constraintValidatorContext)).isFalse();
    }

    @ParameterizedTest
    @CsvFileSource(resources = WRONG_PASSWORDS_CSV, numLinesToSkip = 1)
    void test_IsValidWithSequences_false(String input) {
        assertThat(passwordConstraintsValidator.isValid(input, constraintValidatorContext)).isFalse();
    }

    @ParameterizedTest
    @CsvFileSource(resources = WRONG_PASSWORDS_CSV, numLinesToSkip = 2)
    void test_IsValidWithRepeatedCharacters_false(String input) {
        assertThat(passwordConstraintsValidator.isValid(input, constraintValidatorContext)).isFalse();
    }

    @ParameterizedTest
    @CsvFileSource(resources =WRONG_PASSWORDS_CSV, numLinesToSkip = 3)
    void test_IsValidWithExtraLongPassword_false(String input) {
        assertThat(passwordConstraintsValidator.isValid(input, constraintValidatorContext)).isFalse();
    }

    @ParameterizedTest
    @CsvFileSource(resources =WRONG_PASSWORDS_CSV, numLinesToSkip = 4)
    void test_IsValidWithStrongButShortPassword_false(String input) {
        assertThat(passwordConstraintsValidator.isValid(input, constraintValidatorContext)).isFalse();
    }

    @ParameterizedTest
    @CsvFileSource(resources =WRONG_PASSWORDS_CSV, numLinesToSkip = 5)
    void test_IsValidWithoutLowercase_false(String input) {
        assertThat(passwordConstraintsValidator.isValid(input, constraintValidatorContext)).isFalse();
    }

    @ParameterizedTest
    @CsvFileSource(resources =WRONG_PASSWORDS_CSV, numLinesToSkip = 6)
    void test_IsValidWithoutUppercase__false(String input) {
        assertThat(passwordConstraintsValidator.isValid(input, constraintValidatorContext)).isFalse();

    }

    @ParameterizedTest
    @CsvFileSource(resources =WRONG_PASSWORDS_CSV, numLinesToSkip = 7)
    void test_IsValidWithoutSpecialCharacter_false(String input) {
        assertThat(passwordConstraintsValidator.isValid(input, constraintValidatorContext)).isFalse();
    }

    @ParameterizedTest
    @CsvFileSource(resources =WRONG_PASSWORDS_CSV, numLinesToSkip = 8)
    void test_IsValidWithWhitespace_false(String input) {
        assertThat(passwordConstraintsValidator.isValid(input, constraintValidatorContext)).isFalse();
    }

    @ParameterizedTest
    @MethodSource
    void test_IsValidWithStrongPassword_true(String input) {
        assertThat(passwordConstraintsValidator.isValid(input, constraintValidatorContext)).isTrue();
    }

    public static Stream<String> test_IsValidWithStrongPassword_true() {
        return Stream.of("4mUzu#3otyQpeNRtM%aL", "H!VRMg^BzcVs9CgZ6$v7", "nqw*Ep%8Xac6h*e8vJpy", "UCk!fZ7xoMwxfM%DdKmW");
    }

    private static class PasswordConstraintValidatorTestClass implements Password {

        @Override
        public String message() {
            return "Test Message";
        }

        @Override
        public Class<?>[] groups() {
            return new Class[] { };
        }

        @Override
        public Class<? extends Payload>[] payload() {
            return new Class[] { };
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Password.class;
        }

    }
}