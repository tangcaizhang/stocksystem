/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zzh.project.stocksystem.ui.base;

public interface IView {
    // 是否可用（不是可见）
    boolean isActive();

    // 显示消息
    void showMessage(String msg);

    // 显示错误消息
    void showErrorMessage(String errMsg);

    // 显示loading
    void showLoading();

    // 隐藏loading
    void hideLoading();
}
