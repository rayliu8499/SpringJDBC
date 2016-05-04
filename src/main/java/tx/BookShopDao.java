package tx;

/**
 * Created by ray.liu on 2016/5/4.
 */
public interface BookShopDao {

    int findBookPriceByIsbn(String isbn);

    void updateBookStock(String isbn);

    void updateUserAccount(String username, double price);

}
