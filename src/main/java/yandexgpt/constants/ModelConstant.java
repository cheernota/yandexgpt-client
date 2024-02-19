package yandexgpt.constants;


/**
 * The identifier of the model to be used for completion generation.
 */
public enum ModelConstant {
    /**
     * Core YandexGPT model.
     */
    YANDEX_GPT("yandexgpt"),
    /**
     * YandexGPT Lite model.
     */
    YANDEX_GPT_LITE("yandexgpt-lite"),
    /**
     * Summarization model.
     */
    SUMMARIZATION("summarization");

    private final String modelName;

    ModelConstant(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public static ModelConstant convert(Object obj) {
        if (obj instanceof String) {
            for (ModelConstant constant : ModelConstant.values()) {
                if (constant.getModelName().equalsIgnoreCase(obj.toString())) {
                    return constant;
                }
            }
        }
        return YANDEX_GPT_LITE;  // default
    }

    @Override
    public String toString() {
        return modelName;
    }
}