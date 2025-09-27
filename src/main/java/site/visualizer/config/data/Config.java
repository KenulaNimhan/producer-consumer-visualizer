package site.visualizer.config.data;

public enum Config {
    TOTAL_TICKETS("Total no. of tickets to produce: ", 1, 100),
    BUFFER_CAP("Capacity of the buffer: ", 1, 100),
    VENDOR_COUNT("No. of vendors producing: ", 1, 10),
    CUSTOMER_COUNT("No. of customers buying: ", 1, 10),
    CAP_PER_CUSTOMER("Maximum no. of tickets per customer: ", 1, 10),
    RELEASE_RATE("Rate of releasing tickets: ", 100, 2000),
    RETRIEVAL_RATE("Rate of consuming tickets: ",100,2000);

    private final String prompt;
    private final int[] range = new int[2];

    Config(String prompt, int rangeStart, int rangeEnd) {
        this.prompt = prompt;
        range[0] = rangeStart;
        range[1] = rangeEnd;
    }

    public String getPrompt() {
        return prompt;
    }

    public int[] getRange() {
        return range;
    }

    public boolean rangeAccepts(int val) {
        return (val>=this.getRange()[0] && val<=this.getRange()[1]);
    }
}
