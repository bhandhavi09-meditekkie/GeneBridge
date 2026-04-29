@RestController
public class RootController {
    @GetMapping("/")
    public String index() {
        return "Welcome to GeneBridge! Use /api/health to check status.";
    }
}
