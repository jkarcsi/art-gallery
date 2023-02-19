package jkarcsi.utils.validation;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RepeatCharacterRegexRule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class PasswordConstraintsValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        PasswordValidator passwordValidator = new PasswordValidator(
                Arrays.asList(new LengthRule(10, 128), new CharacterRule(EnglishCharacterData.UpperCase, 1),
                        new CharacterRule(EnglishCharacterData.LowerCase, 1),
                        new CharacterRule(EnglishCharacterData.Digit, 1),
                        new CharacterRule(EnglishCharacterData.Special, 1), new WhitespaceRule(),
                        new RepeatCharacterRegexRule(3),
                        new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
                        new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false)));

        RuleResult result = passwordValidator.validate(new PasswordData(password));

        return result.isValid();
    }
}
