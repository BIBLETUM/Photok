/*
 *   Copyright 2020-2021 Leon Latsch
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package dev.leonlatsch.photok.ui.settings.hideapp

import android.app.Application
import android.content.ComponentName
import android.content.pm.PackageManager
import android.view.View
import androidx.databinding.Bindable
import androidx.hilt.lifecycle.ViewModelInject
import dev.leonlatsch.photok.BR
import dev.leonlatsch.photok.other.empty
import dev.leonlatsch.photok.other.formatLaunchCode
import dev.leonlatsch.photok.settings.Config
import dev.leonlatsch.photok.ui.components.bindings.ObservableViewModel

/**
 * ViewModel for the HideAppDialog. Holds a state.
 *
 * @since 1.2.0
 * @author Leon Latsch
 */
class ToggleAppVisibilityViewModel @ViewModelInject constructor(
    private val app: Application,
    private val config: Config
) : ObservableViewModel(app) {

    @get:Bindable
    var title: String = String.empty //TODO: Use String resource
        set(value) {
            field = value
            notifyChange(BR.title, value)
        }

    @get:Bindable
    var buttonText: String = String.empty // TODO: replace with string resource
        set(value) {
            field = value
            notifyChange(BR.buttonText, value)
        }

    @get:Bindable
    var currentState: String = String.empty
        set(value) {
            field = value
            notifyChange(BR.currentState, value)
        }

    @get:Bindable
    var hintVisibility: Int = View.VISIBLE
        set(value) {
            field = value
            notifyChange(BR.hintVisibility, value)
        }

    var confirmText: String = String.empty

    override fun setup() {
        super.setup()
        if (isMainComponentDisabled()) {
            title = "Show Photok"//TODO: Use String resource
            buttonText = "Show Photok"
            currentState = "HIDDEN"
            hintVisibility = View.GONE
            confirmText = "Show boi?"
        } else {
            title = "Hide Photok"//TODO: Use String resource
            buttonText = "Hide Photok"
            currentState = "VISIBLE"
            hintVisibility = View.VISIBLE
            confirmText = "Hide boi?"
        }
    }

    /**
     * Toggles the visibility.
     * - Case a: Disables [MAIN_LAUNCHER_COMPONENT] enables [STEALTH_LAUNCHER_COMPONENT]
     * - Case b: Disables [STEALTH_LAUNCHER_COMPONENT] enables [MAIN_LAUNCHER_COMPONENT]
     */
    fun toggleMainComponent() {
        if (isMainComponentDisabled()) {
            app.packageManager.setComponentEnabledSetting(
                MAIN_LAUNCHER_COMPONENT,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
            app.packageManager.setComponentEnabledSetting(
                STEALTH_LAUNCHER_COMPONENT,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        } else {
            app.packageManager.setComponentEnabledSetting(
                MAIN_LAUNCHER_COMPONENT,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            app.packageManager.setComponentEnabledSetting(
                STEALTH_LAUNCHER_COMPONENT,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }

    /**
     * Indicated if [MAIN_LAUNCHER_COMPONENT] is currently disabled.
     */
    fun isMainComponentDisabled(): Boolean {
        val enabledSetting = app.packageManager.getComponentEnabledSetting(MAIN_LAUNCHER_COMPONENT)
        return enabledSetting == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    }

    fun secretLaunchCode() = formatLaunchCode(config.securityDialLaunchCode)

    companion object {
        private val MAIN_LAUNCHER_COMPONENT =
            ComponentName("dev.leonlatsch.photok", "dev.leonlatsch.photok.MainLauncher")

        private val STEALTH_LAUNCHER_COMPONENT =
            ComponentName("dev.leonlatsch.photok", "dev.leonlatsch.photok.StealthLauncher")
    }
}