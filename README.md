# Vymo Assignment - Config-driven lead form

A lead capture form in Jetpack Compose that renders itself from a config list, built on a
small in-house design system. No form library is used.

## Build and run

Requirements: Android Studio (recent stable, AGP 9.x), JDK 17+ (the bundled JBR works), Android SDK 37.
Min SDK is 26.

**Android Studio:** open the project folder, let Gradle sync, pick an emulator and run the `app` configuration.

**Command line:**

```bash
./gradlew :app:assembleDebug        # build the APK
./gradlew :app:installDebug         # install on a running emulator/device
./gradlew :app:testDebugUnitTest    # unit tests (validation, view model, row layout)
```

**Emulators used:**

- *Compact:* `Medium Phone` (API 37) in portrait. One column.
- *Expanded:* the same phone rotated to landscape, or a `Pixel Tablet` AVD. Two columns.

The switch happens at 600dp of available width, so any emulator wider than that shows the
two-column layout.

## Package layout

```
com.tlw.vymo.assignment
├── MainActivity.kt
├── designsystem/              # knows nothing about leads
│   ├── tokens/                # VymoColors, VymoSpacing (+ VymoShapes), VymoTypography, VymoBreakpoints
│   ├── theme/                 # VymoTheme: provides the tokens, maps them onto MaterialTheme
│   ├── atoms/                 # VymoTextField, VymoSelect, VymoCheckbox, VymoButton
│   ├── molecules/             # VymoFormField: label + control + hint + error
│   └── form/                  # config model, VymoForm, compact/expanded layouts, row packing
└── lead/                      # the lead feature
    ├── LeadFormConfig.kt      # the field list
    ├── LeadFormValidator.kt   # validateForm(config, values)
    ├── LeadFormViewModel.kt   # values, touched fields, submit state
    ├── LeadFormScreen.kt      # screen, insets, keyboard, submit bar
    └── LeadSubmissionResult.kt  # result page shown after a valid submit
```

## Where things live

| What | Where |
|---|---|
| Tokens (color, spacing, type, two-column width) | `designsystem/tokens/` |
| Atoms | `designsystem/atoms/` |
| Field molecule | `designsystem/molecules/VymoFormField.kt` |
| Form composable | `designsystem/form/VymoForm.kt` |
| Config schema (`FieldConfig`, `FieldType`, `Validation`, `VisibleWhen`) | `designsystem/form/FormConfig.kt` |
| Type to atom mapping | `designsystem/form/FormFieldItem.kt` |
| Lead config | `lead/LeadFormConfig.kt` |
| Validation rules | `lead/LeadFormValidator.kt` |

## Layouts

| Layout | File |
|---|---|
| Choosing compact vs expanded | `designsystem/form/VymoForm.kt` (available width vs `VymoBreakpoints.TwoColumnMinWidth`) |
| Compact: one column, full-width fields | `designsystem/form/CompactFormLayout.kt` |
| Expanded: two columns | `designsystem/form/ExpandedFormLayout.kt`, rows from `packRows` in `FormRows.kt` |
| System bars, keyboard, submit bar, form vs result page | `lead/LeadFormScreen.kt` |

In the expanded layout, half-width fields share a row and full-width fields take a row of their own.
Textarea and checkbox default to full width, so Notes and Consent span the row, and Full name and
Email end up side by side. When Company name is hidden, Phone moves up next to Lead type.

## How it works

- **Adding a field** means adding a `FieldConfig` entry to `LeadFormConfig`. `FormFieldItem` maps
  its `type` to an atom, and `VymoForm` renders one field per visible entry.
- **Conditional fields** use `visibleWhen`. Company name has
  `VisibleWhen(field = leadType, equals = company)`. Hidden fields are not rendered, not validated
  and not submitted.
- **Validation** is one pure function, `validateForm(config, values): Map<String, String>`. The
  atoms only get an `isError` flag and an error string through the molecule. They have no rules.
- **When errors show:** a field shows its error after it loses focus, or after a submit attempt.
  Once it is showing, the error updates while typing. Submit stays enabled, so tapping it with an
  invalid form shows every current error and blocks the submission.
- **Submitting** a valid form replaces the form with a result page listing the values (two
  columns on wide screens). **Edit** (or system back) returns to the filled form, **New lead**
  clears it.
- **State** lives in `LeadFormViewModel`, so values survive rotation, including the switch
  between the one- and two-column layouts.

## Keyboard and insets

The activity is edge-to-edge with `adjustResize`. The screen uses `WindowInsets.safeDrawing`, so
content stays clear of the status bar, navigation bar and display cutout. The submit bar is the
Scaffold's bottom bar and is padded by the IME inset, so it sits right above the keyboard. The
scrollable content ends above the bar, so a focused field is scrolled into view above the keyboard.

## Tests

`app/src/test`:

- `LeadFormValidatorTest`: every rule, plus conditional Company name
- `LeadFormViewModelTest`: when errors appear, submit behaviour, the hidden field being left out, edit and new lead
- `FormRowsTest`: one- and two-column row packing
