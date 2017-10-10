package bigappcompany.com.artistbooking.network;

public final class ApiUrl {
        public static final String BASE_URL = "http://bigappcompany.co.in/demos/artist/";
        private static final String API_KEY = "rsi8197028387";
        public static final String SLIDERS = BASE_URL +"mobile_banner";
        public static final String IMG_TYPES = BASE_URL + "image_types/" + API_KEY;
        public static final String CATEGORIES = BASE_URL + "category";
        public static final String ARTISTS = BASE_URL + "artist_details";
        public static final String ARTIST_PROFILE = BASE_URL + "artist_profile_details/";
        public static final String NEWS = BASE_URL + "newsNnotifications/" + API_KEY+"/NEWS";
        public static final String EVENTS = BASE_URL + "newsNnotifications/" + API_KEY+"NOTIFICATIONS";
        public static final String BOTH = BASE_URL + "newsNnotifications/" + API_KEY+"/BOTH";
        private String unp="rsi:rsi8197028387";
        public static final String IMAGES = BASE_URL + "artist_photos_list/";
        public static final String VIDEOS = BASE_URL + "artist_videos_list/";
        public static final String UPLOAD_IMG = BASE_URL + "customer_profile_pic_update";
        public static final String PRO_DETAILS = BASE_URL+"customer_profile_details/";
        public static final String BOOKINGS = BASE_URL+"event_details_customer/";
        public static final String REGISTER = BASE_URL+"customer_registration";
        public static final String LOGIN = BASE_URL+"customer_login";
        public static final String BOOK = BASE_URL+"event_booking";
        public static final String CITIES = BASE_URL + "city_list";
        public static final String NOTIFICATIONS = BASE_URL + "notifications";
        public String getUnp() {
                return unp;
        }
}
