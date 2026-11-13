AGENTS.md — qnct
Project overview
qnct (pronounced "connect") is a personal-use Android app for sharing contact details and an Instagram profile through QR codes. Tagline: Scan. Share. Connect.

The MVP should be small, reliable, privacy-conscious, and easy to maintain. Prefer the simplest implementation that meets the agreed requirements. Do not build infrastructure for speculative future features.
Agent workflow and approvals
- Plan before implementing. For nontrivial changes, briefly explain the approach, files likely to change, and verification plan before writing code.
- Ask about consequential ambiguities. For minor details, choose sensible defaults and state them.
- Keep changes scoped to the requested task. Avoid unrelated refactoring and unnecessary dependencies.
- Never commit personal contact details, tokens, secrets, signing keys, or local configuration containing sensitive values. Use fictitious sample data in tests and documentation.
- Before reporting completion, run relevant checks where possible and accurately state what passed, failed, or could not be run.

Issue Planning
- Propose and discuss GitHub issues before creating them.
- Never create or modify GitHub issues without my approval.

Issue Implementation
When I explicitly authorize implementation of a GitHub issue:

1. Review the issue requirements and acceptance criteria.
2. Create a dedicated feature branch from the latest main branch.
   - Features: `feature/<issue-number>-<short-description>`
   - Bug fixes: `fix/<issue-number>-<short-description>`
3. Implement only the authorized issue and its necessary dependencies.
4. Run available local tests and validation.
5. Commit changes with descriptive messages.
6. Push the feature branch.
7. Open a pull request referencing the issue, using `Closes #<issue-number>` where appropriate.
8. Monitor the GitHub Actions CI pipeline associated with the pull request.
9. Confirm that all required CI checks pass before reporting the implementation as complete.
10. Provide a final implementation summary, including:
    - Changes made
    - Tests performed
    - CI pipeline results
    - Any limitations or outstanding concerns
    - Pull request URL

CI Verification Requirements
- After opening a pull request, check the actual GitHub Actions workflow results.
- Do not assume CI succeeded simply because the workflow was triggered or local tests passed.
- Wait for the relevant workflows to finish and verify their final status.
- If CI fails because of implementation changes, investigate the logs, fix the problem, commit and push the corrections, and verify the new pipeline run.
- Repeat until all required checks pass or a genuine external blocker is identified.
- If a pipeline fails because of an external issue, explain the cause and provide evidence.
- Never report an issue as fully implemented and verified while required CI checks are failing, pending, or unverified.
- If no applicable CI workflow exists, explicitly report that CI verification is unavailable rather than claiming success.
- Do not merge the pull request.

Restrictions
- Never merge a PR without my explicit approval.
- Never push directly to main.
- Never force-push, rewrite shared history, or delete remote branches without explicit approval.
- Do not implement unrelated issues without authorization.
- Do not create releases or publish artifacts without approval.
- Ask before performing destructive operations or work outside the authorized issue scope.
MVP requirements
Onboarding and profile
- Show one-time setup on first launch.
- Collect full name (required), phone number (optional), email (optional), and Instagram username (optional) through manual entry.
- Validate populated fields and persist profile data locally with Jetpack DataStore.
- After setup, show the Contact tab. On every fresh launch after setup, default to Contact.
- Provide a dedicated Settings screen. Saving changes updates the displayed QR code immediately.
Contact QR
- Encode contact data as vCard 3.0 with full name and optional phone/email fields.
- Escape vCard special characters and handle line breaks correctly.
- Generate QR codes locally without a backend or network access.
- Use a large black-on-white QR with an appropriate quiet zone, including in dark mode.
- Test scanning/import behavior manually on representative Android and iPhone devices; do not claim universal compatibility without testing.
Instagram QR
- Accept an Instagram username and construct https://www.instagram.com/{username}/ after validation.
- Display a QR code for the URL on the Instagram tab.
- When no valid username exists, show a helpful empty state with a path to Settings.
UI and device behavior
- Use two top tabs: Contact and Instagram.
- Follow the system light/dark theme using Material 3.
- Temporarily boost activity-window brightness while a QR code is visible, restoring the prior window behavior afterward. Do not modify global system brightness or request system-settings permissions.
- Keep the interface minimalist, readable, and quick to use.
Technical direction
- Language: Kotlin
- UI: Jetpack Compose + Material 3
- Minimum Android version: Android 10 (API 29)
- Architecture: Lightweight MVVM; keep validation, payload generation, QR encoding, persistence, and UI responsibilities reasonably separated.
- Storage: Jetpack DataStore; no server or user account.
- QR encoding: A well-maintained, compatible library such as ZXing.
- Build: Gradle Kotlin DSL with committed Gradle Wrapper.
- CI: GitHub Actions builds and runs unit tests on pull requests.
- Distribution: Direct APK installation; no Play Store workflow in MVP.
Use stable mutually compatible dependency versions. Follow established repository conventions once code exists. Avoid introducing extra modules, elaborate domain layers, DI frameworks, or abstractions without a concrete need.
Testing expectations
Add or update unit tests for behavior changed by each implementation issue, particularly:
- Required-name and optional-field validation.
- vCard serialization, escaping, and payload correctness.
- Instagram username validation and URL construction.
- QR encoding of the intended payload.
For UI/device-dependent features, document manual verification steps covering onboarding, settings persistence, tab defaults, QR readability, Android/iPhone scans, theme behavior, and brightness restoration.

Run the relevant Gradle build and test tasks before claiming an implementation is complete. If execution is unavailable, report that limitation rather than implying tests passed.
Out of scope for MVP
Do not add NFC, QR image exporting/sharing, multiple profiles, additional social platforms, hosted landing pages, dynamic redirects, backend services, accounts, cloud sync, analytics, contact importing, custom QR styling, or Play Store publishing unless the user explicitly changes scope.
GitHub issue planning format
When asked to plan the backlog, propose roughly 8–15 independently implementable issues. For each issue provide:
1. Action-oriented title.
2. Purpose and concrete scope.
3. Objective acceptance criteria.
4. Automated and/or manual verification.
5. Dependencies on other proposed issues.
6. Relative complexity (S/M/L).
7. Relevant requirements covered.
Favor vertical slices and an early, scannable contact-QR proof of concept. Identify open decisions and risks. Stop after presenting the proposed plan and request approval before making any GitHub mutations.
Source of truth
If the repository includes an approved MVP requirements document, use it alongside this file. Flag conflicts or material gaps for discussion instead of silently overriding approved decisions. User-approved issue scope and explicit instructions take precedence for the task at hand.
