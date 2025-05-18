package org.mozgotrash.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum TgBotConstant {

    ADMIN_USERNAME("Mozgotrash", "Юзернейм админа"),
    CHECK_PROGRESS_BUTTON("button_isThereProgress", "Кнопка проверки прогресса");

    private String code;
    private String description;

}
