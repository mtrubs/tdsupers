package com.mtrubs.td.config;

public interface SettingsManager {

  /**
   * @return true if sound effects are enabled, false disabled.
   */
  boolean isSoundEnabled();

  /**
   * Switched the state of the sound effects setting between enabled and disabled.
   */
  void toggleSoundEnabled();

  /**
   * @return true if music is enabled, false disabled.
   */
  boolean isMusicEnabled();

  /**
   * Switched the state of the music setting between enabled and disabled.
   */
  void toggleMusicEnabled();

  /**
   * @return true if device vibrations are enabled, false disabled.
   */
  boolean isVibrateEnabled();

  /**
   * Switched the state of the vibration setting between enabled and disabled.
   */
  void toggleVibrateEnabled();
}
