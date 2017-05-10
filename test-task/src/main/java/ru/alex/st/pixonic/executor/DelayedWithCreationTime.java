package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.util.concurrent.Delayed;

public interface DelayedWithCreationTime extends Delayed {

    LocalDateTime getCreationTime();
    
}
