package com.gorbachevs.foodly.presentation.common.view.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.gorbachevs.foodly.R
import com.gorbachevs.foodly.presentation.common.animation.IconAnimatedVisibility
import com.gorbachevs.foodly.presentation.common.drawable.FoodlyIcons
import com.gorbachevs.foodly.presentation.common.preview.ThemePreviewParameter
import com.gorbachevs.foodly.presentation.common.theme.FoodlyTheme

@Composable
fun Search(
    value: String,
    onValueChange: (String) -> Unit,
    onValueResetClick: () -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(50.dp)
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = FoodlyIcons.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 20.dp, top = 17.dp, bottom = 17.dp)
                    .size(24.dp)
            )
            SearchTextField(
                value = value,
                onValueChange = onValueChange,
                onValueResetClick = onValueResetClick,
                placeholderText = placeholderText,
                enabled = enabled,
                interactionSource = interactionSource
            )
        }
    }
}

@Composable
private fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onValueResetClick: () -> Unit,
    placeholderText: String,
    interactionSource: MutableInteractionSource,
    enabled: Boolean = true
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)
    val onTextFieldValueChange: (TextFieldValue) -> Unit = { fieldValue ->
        textFieldValueState = fieldValue
        if (value != fieldValue.text) {
            onValueChange(fieldValue.text)
        }
    }

    BasicTextField(
        value = textFieldValue,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        onValueChange = onTextFieldValueChange,
        enabled = enabled,
        readOnly = false,
        visualTransformation = VisualTransformation.None,
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions(),
        interactionSource = interactionSource,
        singleLine = true,
        maxLines = 1,
        minLines = 1,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = @Composable { innerTextField ->
            SearchFieldDecorationBox(
                textFieldValue = textFieldValue,
                innerTextField = innerTextField,
                placeholderText = placeholderText,
                onValueResetClick = onValueResetClick,
                enabled = enabled,
                interactionSource = interactionSource,
                colors = createSearchTextFieldColors()
            )
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
private fun SearchFieldDecorationBox(
    textFieldValue: TextFieldValue,
    innerTextField: @Composable () -> Unit,
    placeholderText: String,
    onValueResetClick: () -> Unit,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    colors: TextFieldColors
) {
    TextFieldDefaults.TextFieldDecorationBox(
        value = textFieldValue.text,
        visualTransformation = VisualTransformation.None,
        innerTextField = innerTextField,
        placeholder = { Text(placeholderText) },
        label = null,
        leadingIcon = null,
        trailingIcon = {
            IconAnimatedVisibility(visible = textFieldValue.text.isNotEmpty()) {
                IconButton(
                    onClick = onValueResetClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = FoodlyIcons.Close,
                        contentDescription = null,
                    )
                }
            }
        },
        singleLine = true,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = colors,
        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 0.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun createSearchTextFieldColors(): TextFieldColors = TextFieldDefaults.textFieldColors(
    textColor = MaterialTheme.colorScheme.onSecondaryContainer,
    disabledTextColor = MaterialTheme.colorScheme.secondary,
    containerColor = Color.Transparent,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
    unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
    disabledLeadingIconColor = MaterialTheme.colorScheme.primary,
    disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
    placeholderColor = MaterialTheme.colorScheme.secondary,
    disabledPlaceholderColor = MaterialTheme.colorScheme.secondary,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent
)

@Preview
@Composable
private fun Preview(@PreviewParameter(ThemePreviewParameter::class) isDarkMode: Boolean) {
    FoodlyTheme(darkTheme = isDarkMode) {
        Column(Modifier.background(MaterialTheme.colorScheme.background)) {
            Search(
                value = "",
                onValueChange = {},
                onValueResetClick = {},
                placeholderText = stringResource(R.string.search)
            )

            Spacer(Modifier.height(64.dp))

            Search(
                value = "Example search value",
                onValueChange = {},
                onValueResetClick = {},
                placeholderText = stringResource(R.string.search),
            )
        }
    }
}
