import json
import random


class Restaurant:
    def __init__(self, name, foods, image = None):
        self.name = name
        self.foods = foods
        self.image = image
        self.categories = set()
        for food in foods:
            self.categories.add(food.category)

    def to_dict(self):
        return { 'name': self.name, 'foods': [food.to_dict() for food in self.foods],
                'categories': list(self.categories), 'image': self.image }
    
class Food:
    def __init__(self, name, category, image, description, price = 0, rate = 1):
        self.name = name
        self.image = image
        self.category = category
        self.price = price
        self.rate = rate
        self.description = description

    def to_dict(self):
        return { 'name': self.name,'category': self.category, 'image': self.image,
                'price': self.price, 'rate': self.rate, 'description': self.description}

N = 5       # No of restaurants
m = 50      # Minimum cost
M = 700     # Maximum cost

menu = {}
foodCategories = ['vegstarters','nonvegstarters','vegmaincourse','nonvegmaincourse',
           'beverages', 'sweets', 'rolls', 'rice', 'breads']

foods = [Food('Corn Cheese balls','vegstarters','https://i2.wp.com/www.vegrecipesofindia.com/wp-content/uploads/2016/11/cheese-corn-balls-recipe-2-1.jpg?fbclid=IwAR0zPi_Mgl8sxetnooyxpiAMEUe6e-Zep5Gyl0NmNJvqbs2RYss3wIaQecw','Sorry description not available for this item'),
        Food('Paneer Tikka','vegstarters','https://kathmandupalace.co.uk/order/assets/img/photos/menu-sample-burgers.jpg','Sorry description not available for this item'),
        Food('Veg Balls','vegstarters','https://5.imimg.com/data5/HC/EA/MY-67343723/frozen-veg-finger-500x500.jpg','Sorry description not available for this item'),
         Food('Gobi 65','vegstarters','https://www.vgeteasy.com/wp-content/uploads/2019/07/gobi-65-recipe-1.jpg','Sorry description not available for this item'),
         Food('Chicken Lollipop','nonvegstarters','https://www.amoncarclassiccaterers.com/web/wp-content/uploads/2015/11/nonvegstarters.jpg','Sorry description not available for this item'),
         Food('Tandoori','nonvegstarters','https://lh3.googleusercontent.com/proxy/nBv8a7isMbBVYKN-DtHlcKClqhIw0oEbiFfU8aGIJx_1Z2Jlk2gTzvrNHc1TlNcQVhciyzrrcB4MvJo','Sorry description not available for this item'),
         Food('Chicken Tikka','nonvegstarters','https://lh3.googleusercontent.com/proxy/BsrY5A-1XdVU8XhU3MMMQq3DkNXzPtsCZhIIPh7qXYyUy5Op3fK9JwRWRbA0NkzUBnTX-_kA98LvpdfEnZGn-f6Irypr1Mh9M7TBKkPWU8kJpgO8_eLOvAlZchH_9zzuP8ixYAf0lc8','Sorry description not available for this item'),
         Food('Prawns Fry','nonvegstarters','https://b.zmtcdn.com/data/reviews_photos/c75/5c6d04dc75a2715d09bad9814df0fc75_1560910197.jpg','Sorry description not available for this item'),
         Food('Grilled Chicken','nonvegstarters','https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Chickentandoori.jpg/200px-Chickentandoori.jpg','Sorry description not available for this item'),
         Food('Paneer Tikka Masala','vegmaincourse','https://i0.wp.com/cookingfromheart.com/wp-content/uploads/2017/03/Paneer-Tikka-Masala-3.jpg?resize=1024%2C755&ssl=1','Sorry description not available for this item'),
         Food('Paneer Butter Masala','vegmaincourse','https://recipes.timesofindia.com/thumb/53098531.cms?width=1200&height=1200','Sorry description not available for this item'),
         Food('Veg Kurma','vegmaincourse','https://www.kannammacooks.com/wp-content/uploads/Hotel-Saravana-Bhavan-Chapati-Parotta-Vegetable-Kurma-Recipe-1-4.jpg','Sorry description not available for this item'),
         Food('Veg Manchurian Gravy','vegmaincourse','https://www.cookforindia.com/wp-content/uploads/2016/05/veg_manchurian_gravy.jpg','Sorry description not available for this item'),
         Food('Dhal','vegmaincourse','https://www.thespruceeats.com/thmb/xMLcsG5G8IgzJB9UT4nDnZ4L0IE=/1000x1000/smart/filters:no_upscale()/spicy-lentil-dahl-recipe-1001539-4-4_preview-5b0ec926eb97de0037890d0b.jpeg','Sorry description not available for this item'),
         Food('Chicken Curry','nonvegmaincourse','https://tasteasianfood.com/wp-content/uploads/2016/01/chicken-curry-250-square.jpg','Sorry description not available for this item'),
         Food('Fish Curry','nonvegmaincourse','https://www.corriecooks.com/wp-content/uploads/2018/09/Instant-Pot-Red-Hot-Chili-Fish-Curry-500x500.jpg','Sorry description not available for this item'),
         Food('Royal Pulusu','nonvegmaincourse','https://i.ytimg.com/vi/iuno5cW4maA/maxresdefault.jpg','Sorry description not available for this item'),
         Food('Kadhai Chicken','nonvegmaincourse','https://mariasmenu.com/wp-content/uploads/Kadai-Chicken.png','Sorry description not available for this item'),
         Food('Malabar Chicken Curry','nonvegmaincourse','https://www.whiskaffair.com/wp-content/uploads/2019/08/Malabar-Chicken-Curry-5.jpg','Sorry description not available for this item'),
         Food('Basmati rice','rice','https://www.mygorgeousrecipes.com/wp-content/uploads/2019/01/How-to-Cook-Basmati-Rice-to-Perfection-1.jpg','Sorry description not available for this item'),
         Food('Plain Rice','rice','https://storcpdkenticomedia.blob.core.windows.net/media/recipemanagementsystem/media/recipe-media-files/recipes/retail/x17/18484-garlic-herb-rice-760x580.jpg?ext=.jpg','Sorry description not available for this item'),
         Food('Prawn Fried Rice','rice','https://sweetandsavorymeals.com/wp-content/uploads/2019/12/Shrimp-Fried-Rice-2.jpg','Sorry description not available for this item'),
         Food('Chicken Cauliflower Rice','rice','https://hips.hearstapps.com/del.h-cdn.co/assets/17/19/1494521648-chicken-fried-cauliflower-rice-2.jpg','Sorry description not available for this item'),
         Food('Garlic Nan','breads','https://hostthetoast.com/wp-content/uploads/2018/08/naan-202-320x320-1.jpg','Sorry description not available for this item'),
         Food('Chapatti','breads','https://cdn3.tmbi.com/toh/GoogleImagesPostCard/exps136906_CW153045B08_20_7b.jpg','Sorry description not available for this item'),
         Food('Coca Cola','beverages','https://www.vynoguru.lt/media/catalog/product/cache/2/image/800x600/9df78eab33525d08d6e5fb8d27136e95/c/o/coca_cola_2.0_l.jpg','Sorry description not available for this item'),
         Food('Thumbs up','beverages','https://5.imimg.com/data5/EQ/IE/ZU/SELLER-48652903/250ml-thums-up-500x500.jpg','Sorry description not available for this item'),
         Food('Chicken Roll','rolls','https://recipes.timesofindia.com/thumb/53210540.cms?width=1200&height=1200', 'Yummy minced chicken rolled in pitabread'),
         Food('Shawarma','rolls','https://www.wellplated.com/wp-content/uploads/2015/10/Slow-Cooker-Chicken-Shawarma-recipe.-A-garlic-yogurt-chicken-with-warm-spices-that-tastes-just-like-Greek-chicken-shawarma-but-less-work.jpg','Sorry description not available for this item'),
         Food('Veg Pakkoda','vegstarters','https://res.cloudinary.com/hksqkdlah/image/upload/v1529589364/41702-sfs-crispy-vegetable-fritters-31.jpg','Sorry description not available for this item'),
        Food('Veg Cutlets', 'vegstarters', 'https://www.spiceupthecurry.com/wp-content/uploads/2018/03/bread-roll-recipe-12-360x360.jpg', 'Vegetable Cutlet is a snack, It is crisp from outside and soft from inside and one of the best evening tea time snacks. Cooked with vegetables like potatoes, fansi, carrots and cabbage in oil and perked it up with spices and other flavourful ingredients.'),
Food('Baby Corn', 'vegstarters', 'https://www.spiceupthecurry.com/wp-content/uploads/2018/03/bread-roll-recipe-12-360x360.jpg', 'Baby corn is a super crunchy & crispy Indo Chinese starter recipe, similar to manchurian and chilli recipes but super crunchy and less spicy'),
Food('chilli Mushroom', 'vegstarters' , 'https://i.pinimg.com/originals/00/2c/df/002cdf0ba521a41557637c88b4532d62.jpg', 'Chilli mushroom is a popular indo chinese starter recipe or a street food snack prepared with deep fried mushroom and chilli based sauce'),
Food('Mysore bonda', 'vegstarters', 'https://www.indianhealthyrecipes.com/wp-content/uploads/2016/04/mysore-bonda-recipe-1-500x375.jpg', 'Mysore bondas are fried dumplings made with flour, yogurt and spices. They are crispy outside and fluffy'),
Food('Potato lollipops', 'vegstarters', 'https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQInaqDAyZ4toSC6NizkKUr3OaIZcEYaPMkNEiRuTmwxC1XKwDo&usqp=CAU', 'Spicy potato mixture shaped into balls, deep fried and served inserted on toothpicks'),
Food('veg chops', 'vegstarters', 'https://i2.wp.com/www.vegrecipesofindia.com/wp-content/uploads/2016/12/veg-chops-recipe-2-1-500x500.jpg', 'Veg Chops is a cutlet- like snack that is very popular in Bengal. This a healthy, vegetarian version of this evergreen snack, made with meldey of yummy veggies like potatoes, beetroot and carrots perked up with onions and spices to make it super tasty.'),
Food('Veg cuttings', 'vegstarters', 'https://www.cityshor.com/uploads/article/11_2015/1447930330_2.jpg', 'Veg cuttings is vegetarian starter,vmade with gramm flour and add with seasonable veggies in and fried in oil.'),
Food('Cucumber Stir Fry', 'vegstarters', 'https://www.archanaskitchen.com/images/archanaskitchen/1-Author/sibyl_sunitha/Udupi_Style_Southekayi_Palya_Recipe_Mangalore_Cucumber_Stir_Fry_Recipe_.jpg', 'Cucumber stir fry is famous Karnataka starter, made with cumber, sesame seeds and coconut flakes, and fired in ghee '),
Food('Mexican veg party', 'vegstarters', 'https://www.archanaskitchen.com/images/archanaskitchen/1-Author/sibyl_sunitha/Udupi_Style_Southekayi_Palya_Recipe_Mangalore_Cucumber_Stir_Fry_Recipe_.jpg','Sorry description not available for this item' ),
Food('Crispy popcorn', 'vegstarters', 'https://i0.wp.com/flavouroma.com/wp-content/uploads/2018/03/crispy-corn-recipe-barbecue-nation-style-2.jpg?resize=840%2C560', 'Crispy popcorn is BBQ nation style staryter, made with corn kernels are half cooked, coated with flours, then deep fried finally tossed with some spice powders, onion and lemon juice'),
Food('Mushroom 65', 'vegstarters', 'https://s3-ap-southeast-1.amazonaws.com/sb-singleserver-prod-bucket/245dced42d3b5e0665411e09d4e77a4a/o_1493802118.jpg', 'Crispy and tasty mushroom 65 is a delicious starter snack that can be served with any Indian main course meal'),
Food('Samosa', 'vegstarters', 'https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQK8inDlYBZNn0fZBk_Svul57PLLmeb1vMHkivplm987-Ic4ajr&usqp=CAU', 'A samosa is a fried or baked pastry with a savoury filling, such as spiced potatoes, onions, peas, cheese,'),
Food('Stuffed Mirchi', 'vegstarters', 'https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQK8inDlYBZNn0fZBk_Svul57PLLmeb1vMHkivplm987-Ic4ajr&usqp=CAU', 'The addition of hing along with besan and subtle spics brings out the flavours of this sabsi making it perfect for a rainy day lunch or dinner.'),
Food('Chutney Bombs', 'vegstarters', 'https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQK8inDlYBZNn0fZBk_Svul57PLLmeb1vMHkivplm987-Ic4ajr&usqp=CAU', 'These bite size snack balls are full of flavors, aroma and taste vegetarian appetizer'),
Food('Fish Fry', 'nonvegstarters', 'https://adm.fifthseason.com.sg/Dynamic/Products/45/Images/DNVST2100%20CHILLI%20FISH%20DRY.jpg', 'Fish fry is made with adding basen and Indian spices and salt and fried in pan'),
Food('Fish Chili Dry' , 'nonvegstarters', 'https://lh3.googleusercontent.com/proxy/Cvc2H7qAbf29po4PBmgIbH_vB6bmXvavPiKp__lpeQy6pN7TsOU5McdHFc12YXHUtcY-_1Mb3PDRkeznIwTu5vFHAeH4KuP335_sEprHtJYGJ4YTKiu3nx43M--LQTYFgetyCHaOzdRU8ZaFIDMxD39tSg', 'hilli fish is a popular Indian-chinese appetizer made by tossing fish in spicy'),
Food('Fish 65', 'nonvegstarters', 'https://i.ytimg.com/vi/5lnfGtEX96Y/maxresdefault.jpg', 'Fish 65 is an appetizer or starter made with fleshy fish meat coated with spice mix deep fried to golden'),
Food('Fish roast', 'nonvegstarters', 'https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSRw6BFOYnPp7DtyLYnSRUrNOHEYaR8T4tZd_cHwjnl2NpG4sxE&usqp=CAU', 'Spicy and delicious fish roast which has a simple marinate all over it. The fish slices are coated with spicy masala and then fried until crisp'),
Food('Chicken Majestic', 'nonvegstarters', 'https://1.bp.blogspot.com/-8Jx7PfL13uY/UjmMDL4dxnI/AAAAAAAAXxU/QJ_g7xwBc7g/s1600/IMG_7573.JPG', 'Chicken majestic is an Andhra style chicken starter recipe thats a dry preparation which is spicy'),
Food('Tandoori Platters Combo', 'nonvegstarters', 'https://lh3.googleusercontent.com/proxy/-YFif2NyGihMd6SCjMzIdA8G2UZ4PD9vptt6bBaEQDKIUf2BWnmLo70--9uqdF7jVxCKue4UnCmtUBapQj4H7-ehOOhVwEgY2U3oCrvpt783RcifsZVLoVal9MO_hsQgfAo', 'a combo platter of various chicken and vegetable items baked in a tandoor oven. Regulars like the murg makhni (butter chicken)'),
Food('Chili Chicken', 'nonvegstarters', 'https://feenix.co.in/wp-content/uploads/2018/01/Crispy-Honey-Chilly-Chicken-4.jpg', 'Chili chicken is a sweet, spicy& slightly sour crispy appetizer made with chicken, bell peppers, garlic, chilli sauce & soya sauce' ),
Food('Indo Chinese crispy chicken', 'nonvegstarters', 'https://www.archanaskitchen.com/images/archanaskitchen/1-Author/Pooja_Thakur/chicken_chilli_400.jpg', 'Indo Chinese Crispy Chicken Chilli is a spicy dish which is prepared using chicken and spicy Chinese sauces'),
Food('Chinese Orange chicken', 'nonvegstarters', 'https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQigfn3ugPOQPdSyehZjGEBDolqn4QSqPie9E6UN4La9nIBywgX&usqp=CAU', 'This Chinese Orange Chicken is made with boneless skinless chicken breast, cut into bite-size pieces, dredged, and then fried until golden and crispy.'),
Food('Tandoori Tikka Boti', 'nonvegstarters', 'https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQKUTgNUvsMHgkKg2WB3qQYrs3idQ9uXwNLEtQcCsX-kO5pSavW&usqp=CAU', 'Mildly flavored bone-in chicken marinated in fresh herbs & spices,grilled on skewer'),
Food('Beef Tikka Boti', 'nonvegstarters', 'https://lh3.googleusercontent.com/proxy/TdcHoBMoJeYufRIobsRtnjXpiS0QJCqkFIGuazbawX23PIzrodzIIMCOS_SUr31cl4-yvjppdFc7LVwA1RsR-YoW835rcTOfAxSg-NVhH8xQP9RZxKX3Mw', 'Mildly flavored bone-in beef marinated in fresh herbs & spices,grilled on skewer'),
Food('Chitinnadu meen varuval', 'nonvegstarters', 'https://i0.wp.com/www.sujiscooking.com/wp-content/uploads/2015/07/chettinad-meen-varuval-recipe.1024x1024.jpg?fit=1024%2C682&ssl=1', 'Chettinad fish fry preaped using King fish steaks, a spicy marinade masala coating, and shallow fried until crisp and golden brown'),
Food('Egg Pakkoda', 'nonvegstarters', 'https://www.indianhealthyrecipes.com/wp-content/uploads/2017/11/chicken-fry-500x375.jpg', 'gg Pakora is a North Indian recipe made with eggs, meat masala, gram flour or besan, chilli flakes, and dry mango powder'),
Food('Mutton Masala Vaada', 'nonvegstarters', 'https://www.awesomecuisine.com/wp-content/uploads/2015/08/keema_vadai.jpg', 'Masala Vadai is a popular South Indian snack typically served as a starter or as an evening snack. This variation is made using mutton keema. Serve with chutney or sauce'),
Food('Mutton Keema Balls', 'nonvegstarters', 'https://i.pinimg.com/originals/46/9b/ae/469baecc2e58e9e54d6952b04d5b334c.jpg', 'Mutton keema pre cooked and combined with a flavoursome freshly ground masala and deep fried'),
Food('Malai Kofta', 'vegmaincourse', 'https://www.thecuriouschickpea.com/wp-content/uploads/2018/03/malaikofta-4-720x720.jpg', 'Malai kofta is a popular Indian vegetarian dish made of potato & paneer balls dunked in smooth, rich & creamy gravy'),
Food('Toffu Tikka Masala', 'vegmaincourse', 'https://www.ambitiouskitchen.com/wp-content/uploads/2017/06/tofucurry-1.jpg', 'ofu Tikka Masala is a delicious vegetarian curry which goes well with naan, rice or any brad of your choice.'),
Food('Veg Makhanwala', 'vegmaincourse', 'https://www.archanaskitchen.com/images/archanaskitchen/0-Archanas-Kitchen-Recipes/2019/Vegetable_Makhanwala_Recipe_North_Indian_Punjabi_1.jpg', 'Veg Makhanwala is an utterly butterly delicious curry made with veggies, butter, cream, and aromatic spices.'),
Food('Veg Kadai', 'vegmaincourse', 'https://www.yummyoyummy.com/wp-content/uploads/2015/08/Kadai-vegetables3.jpg', 'Veg kadai is a delicious Indian vegetarian dish comprising of a mixture of veggies in cooked in a gravy flavored with a special kadai masala.'),
Food('Mix Vegetable Curry', 'vegmaincourse', 'https://sodilicious.com/wp-content/uploads/2019/01/Mix-Veg-Sodilicious.jpg', 'Mix veg curry is made by cooking a mixture of vegetables together in a traditional Indian onion-tomato gravy.'),
Food('Dhal Makhani', 'vegmaincourse', 'https://5.imimg.com/data5/FB/DS/MY-18691830/dal-makhani-500x500.png', 'Dal makhani is a classic Indian dish made with whole urad dal, rajma, butter and spices.'),
Food('Kothu cauliflower curry', 'vegmaincourse', 'https://www.spiceindiaonline.com/wp-content/uploads/Kothu-Cauliflower-Curry-1.JPG', 'Kothu Cauliflower is similar to minced meat or kheema except its the veggie version. Finely minced cauliflower florets goes into a thick garam masala making it great side for idli, dosa, chapathi or even as a side for rice.'),
Food('Mushroom Gravy', 'vegmaincourse', 'https://cdn2.stylecraze.com/wp-content/uploads/2014/09/Mushroom-Curry.jpg', 'The vegetarian mushroom gravy is good for baked potatoes or sweet potatoes,mushroom,biscuits and gravy, or over rice, quinoa, or toast. Its also pretty great on its own'),
Food('Mungodi Curry', 'vegmaincourse', 'https://thefoodfairy.files.wordpress.com/2012/09/dsc_0114.jpg', 'his is a delicious and simple mangodi ki sabzi made with moong dal dumplings.'),
Food('Channa Masala', 'vegmaincourse', 'https://www.sigmalive.com/en/uploads/images/news/chana9.jpg', 'Chana Masala made with raw chickpeas which are boiled & then cooked in a onion-tomato masala with spices.'),
Food('Sanja Punjabi', 'vegmaincourse', 'https://www.surreyapp.com/Uploads/MenuImage/fbeb2b57.jpg', 'This Punjabi red gravy is an all-time favorite on the menus of almost all restaurants because they always make this gravy ready and make different dishes from it by little variations.'),
Food('Panner Ghee roast', 'vegmaincourse', 'https://i.ytimg.com/vi/yTAmdhX3egA/maxresdefault.jpg', 'Paneer Ghee Roast is a fiery red, tangy, and spicy gravy with a flavour of clarified butter or ghee in which the spices are roasted.'),
Food('Tawa Paneer Masala', 'vegmaincourse', 'https://i2.wp.com/www.dhabastyle.com/wp-content/uploads/2017/05/Tawa-Paneer-Website.jpg?resize=500%2C500&ssl=1','Sorry description not available for this item'),
Food('Sambar', 'vegmaincourse', 'https://thefamiliarkitchen.com/wp-content/uploads/2019/01/img_5023.jpg', 'sambar or sambhar is a lentil based stew mixed with vegetable and tamarind juice.'),
Food('Okra Curry', 'vegmaincourse', 'https://www.onlyfoods.net/wp-content/uploads/2015/05/Okra-Curry.jpg', 'Okra curry is a Indian style okra stir fry cooked with onion, tomatoes and spices'),
Food('Gosht Karahi', 'nonvegmaincourse', 'https://photo-cdn.urdupoint.com/show_img_new/cooking/cooking_images/570x400/1432194904_img2828.jpg._3', 'he Karahi Gosht is slow cooked in a curry that is infused with spices including kala jeera (black cumin seeds), tomatoes and fresh chillies. The dish is garnished with fresh coriander, slivers of fresh ginger and chopped green chillies. Serve with roti, naan or pulao.'),
Food('Mutton Kasha', 'nonvegmaincourse', 'https://sherebengal.com/wp-content/uploads/2017/09/Mutton-kasha-4pcs.jpg', 'Mutton Kasha is a delicious spicy Bengali Mutton Curry which is full of flavours from mutton, spices and mustard oil.'),
Food('Beef Kadai', 'nonvegmaincourse', ' https://arindianrestaurant.co.nz/wp-content/uploads/2020/02/beef-kadai.jpg', 'Kadai Beef. Succulent pieces of beef, slow cooked chunky tomato curry tempered with coriander and mustard seeds.'),
Food('Andhra Cheppala Pulusu', 'nonvegmaincourse', 'https://www.watscooking.com/images/dish/large/nell.jpg', 'Chepala pulusu is an Andhra fish curry, which is a famous spicy seafood recipe, prepared by cooking fresh fish pieces in tangy tamarind sauce'),
Food('Chittinad Chicken', 'nonvegmaincourse', 'https://feenix.co.in/wp-content/uploads/2018/01/4033ec43408dd69ead59e4f9502a07d7.jpg', 'A popular chicken curry, cooked in a peppery authentic chettinad paste laced with coconut and onions. Bring some magic to your plate with this southern style'),
Food('Andhra Chicken Curry', 'nonvegmaincourse', 'https://i.ytimg.com/vi/6CxcXYKOfHQ/maxresdefault.jpg', 'Andhra Chicken Curry Recipe is the most famous traditional chicken curry in the Andhra region. Its simply made with chicken and few spicy ingredients in it'),
Food('Nalli Nihari', 'nonvegmaincourse', 'https://i.pinimg.com/originals/3c/fb/b1/3cfbb1826523beadea2cc811b916911b.jpg', 'Nalli Nihari is a deliciously smooth flour based stew with slow cooked mutton in a myriad of spices.'),
Food('Mughali Mutton', 'nonvegmaincourse', 'https://monishgujral.com/new/wp-content/uploads/2013/10/Easy-Mutton-Curry-Mughal-Style1.jpg', 'his Old Delhi Style Mughlai Mutton Stew recipe is a royal, delicious assemblage of fine flavours brewed with mutton and spices'),
Food('Punjabi Mutton', 'nonvegmaincourse', 'https://recipes.timesofindia.com/thumb/63201465.cms?width=1200&height=1200', 'Punjabi Mutton Curry is a succulent dish that has tender meat and goes well with all Indian breads such as tandoori roti, butter roti'),
Food('Mutton Sukka', 'nonvegmaincourse', 'https://files2.hungryforever.com/wp-content/uploads/2016/01/28103611/Mutton-Sukka.jpg', 'Mutton Sukka Fry is a lip smacking mutton/lamb recipe where the soft and succulent mutton pieces are coated with aromatic blend of masalas'),
Food('Rui Macher Kalia', 'nonvegmaincourse', 'https://img-global.cpcdn.com/recipes/4947ec1b6a08e0c5/751x532cq70/rui-macher-kalia-rohu-fish-curry-recipe-main-photo.jpg', 'Macher Kalia is a famous Bengali fish curry. Typical Bengali Kalia calls for big chunks of veggies and also shallow-frying of the spices.'),
Food('Mutton Ladpura', 'nonvegmaincourse', 'https://res.cloudinary.com/swiggy/image/upload/f_auto,q_auto,fl_lossy/p2nxfyht8qto2xamlzp1', 'Macher Kalia is a famous mutton curry. Typical Bengali Kalia calls for big chunks of veggies and also shallow-frying of the spices.'),
Food('Malai Mutton', 'nonvegmaincourse', 'https://www.corriecooks.com/wp-content/uploads/2018/09/Instant-Pot-Indian-Malai-Mutton-Curry-500x500.jpg', 'Mutton malai boti is small boneless pieces of mutton marinated in a paste of cream,yogurt,chilies,ginger garlic paste,and fresh coriander.'),
Food('Mutton Rajasthani', 'nonvegmaincourse', 'https://prod-c2i.s3.amazonaws.com/articles/147746668858105a404544d.jpg', 'Rajasthani Laal Maans is a tasty meat curry from Rajasthan, India. It is a mutton curry prepared in a sauce of curd, and red chilies.'),
Food('Savory Rice', 'rice', 'https://occassionshinercatering.co.za/wp-content/uploads/2017/09/Savory-Rice.jpg', 'Full of tasty vegetables and ready from start to finish in less than thirty minutes. A perfect week night dinner.'),
Food('Sara Bunefelds spicy rice', 'rice', 'https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe_images/recipe-image-legacy-id--47781_12.jpg?itok=aUs6QR5D', 'Sara Buenfelds delicious spicy rice is perfect served with your favourite curry'),
Food('Veg Puloav', 'rice', 'https://i2.wp.com/www.vegrecipesofindia.com/wp-content/uploads/2016/07/tahiri-top-rice-recipes7.jpg', 'Pulao is a one pot meal made with basmati rice, mix vegetable, spices.'),
Food('Egg Biriyani', 'rice', 'https://i.ndtvimg.com/i/2017-09/basmati-rice_620x350_71506598066.jpg', 'fragrant basmati rice cooked with aromatic biryani spices, herbs & boiled eggs to yield a delicious one pot egg biryani'),
Food('Fried rice', 'rice', 'https://i2.wp.com/www.vegrecipesofindia.com/wp-content/uploads/2016/07/fried-rice-top-rice-recipes33.jpg', 'Fried Rice is a combination of long grained rice, mixture of warm peas, carrots and onions with scrambled eggs mixed all together'),
Food('Mango rice', 'rice', 'https://honestcooking.com/wp-content/uploads/2011/09/Paaka-Shaale.jpg', 'Sweetened coconut milk flavors sticky rice, which is then served with fresh mango in this deliciously refreshing take on the traditional Thai treat.'),
Food('Hyderabadi chicken  Dum Biriyani', 'rice', 'https://i.pinimg.com/originals/2c/ca/d5/2ccad553273808b29d4b05573eb5b7cb.jpg', 'Hyderabadi chicken biryani is an aromatic, mouth watering and authentic Indian dish with succulent chicken in layers of fluffy rice,'),
Food('Hyderabad Mutton Dum Biriyani', 'rice', 'https://www.whiskaffair.com/wp-content/uploads/2018/02/Hyderabadi-Mutton-Biryani-6.jpg', 'Hyderabadi Mutton Dum Biryani is an extremely delicious dish with a subtle taste where raw mutton is cooked together with half cooked rice with curd and all Indian spices.'),
Food('Mushroom Biriyani', 'rice', 'https://i2.wp.com/www.vegrecipesofindia.com/wp-content/uploads/2018/09/mushroom-biryani-recipe-1.jpg', 'Vegan Mushroom Biryani made in the Instant Pot! Fragrant basmati rice cooked with spices, mushrooms, fresh herbs makes this a wonderfully'),
Food('Poori', 'breads', 'https://cdn3.foodviva.com/static-content/food-images/roti-paratha-recipes/masala-puri-recipe/masala-puri-recipe.jpg', 'Poori is a crispy, golden, deep-fried Indian bread that you can serve with any dish.'),
Food('Parrota', 'breads', 'https://www.shanmugas.com/Content/upload/Parota%20Kuruma.jpg', 'A Parotta, also called is a layered flatbread,'),
Food('Aloo Parrota', 'breads', 'https://holycowvegan.net/wp-content/uploads/2017/03/easy-aloo-paratha-2.jpg', 'Aloo Paratha Aloo paratha is a whole wheat flatbread which is stuffed with spicy mashed potatoes.'),
Food('Kuthu Parrota', 'breads', 'https://files2.hungryforever.com/wp-content/uploads/2017/06/26110331/egg-kothu-parotta.jpg', 'Kothu Parotta, a shredded flaky multi layered Indian flatbread stir fried with assorted vegetables/egg/chicken.'),
Food('Tandoori Roti', 'breads', 'https://www.ektaindianrestaurant.com/fishtown/br/wp-content/uploads/sites/5/2017/02/maxresdefault-8.jpg', 'Tandoori Roti is an Indian bread which was traditionally made in clay ovens or Tandoor.'),
Food('Rumali Roti', 'breads', 'https://www.cosmicbites.com/wp-content/uploads/2013/12/roomali-roti.jpg', 'Rumali roti is one of the unleavened Indian breads that is made traditionally in the Awadhi, Mughlai and Hyderabadi cuisine.'),
Food('Mango Lassi', 'beverages', 'https://www.everyday-delicious.com/wp-content/uploads/2019/06/mango-lassi-recipe-indian-yogurt-drink-with-mango-koktajl-jogurtowy-z-mango-everyday-delicious-1-500x500.jpg', 'The most refreshing drink on earth, or second best to a cold beer. Blitz up mangoes with yogurt, cardamom, lime and honey'),
Food('Sugar Cane Juice', 'beverages', 'https://media-cdn.tripadvisor.com/media/photo-s/11/63/0d/53/sugarcane-juice.jpg', 'Sugarcane juice is the liquid extracted from pressed sugarcane.'),
Food('Masala Chai', 'beverages', 'https://indianetzone.files.wordpress.com/2019/01/drink-e1547641712442.jpg?w=700', 'Masala chai is a flavoured tea beverage made by brewing black tea with a mixture of aromatic Indian spices and herbs.'),
Food('Lemonade', 'beverages', 'https://www.harighotra.co.uk/images/recipes/hero/nimbupani_hero.jpg', 'Lemonade is a very refreshing drink, and this is the best one ever'),
Food('Sharabath', 'beverages', 'https://yummyindiankitchen.com/wp-content/uploads/2016/04/basil-seed-drink-sabja-seeds-recipe.jpg', 'The preparation of this kulukki sarbath is the speciality of the drink.This drink has a mix of sweet,salt and spicy taste served with lemon,green chilli'),
Food('Badhusha', 'sweets', 'https://i.pinimg.com/originals/79/f7/5b/79f75b36f6f7c0ca34e938bdf9174fc5.jpg', 'Bhadhusa is a traditional indian sweet dessert recipe prepared mainly with maida or all-purpose flour, deep fried in ghee / oil and soaked in sugar syrup.'),
Food('Motichoor Laddoo', 'sweets', 'https://static.toiimg.com/thumb/55048059.cms?width=1200&height=1200', 'Popular Indian sweet, Motichoor Ladoo has tiny pearls of boondi (made from gram flour) fried in ghee and then mixed with sugar syrup'),
Food('Kalakand ', 'sweets', 'https://www.indianhealthyrecipes.com/wp-content/uploads/2018/08/kalakand-recipe-500x500.jpg', 'Kalakand is a popular Indian sweet made by reducing milk and sugar.'),
Food('Soan Papdi', 'sweets', 'https://i.pinimg.com/originals/20/a9/42/20a942a892cb097280fa4ac000e9c325.jpg', 'Soan papdi is a popular Indian dessert. It is usually cube-shaped or served as flakes, and has a crisp and flaky texture.'),
Food('Gulab Jamun', 'sweets', 'https://www.masala.tv/wp-content/uploads/2016/07/1-1.jpg', 'Gulab jamun are soft delicious berry sized balls made of milk solids, flour & a leavening agent. These are soaked in rose flavoured sugar syrup.')
 ]

# Generating a menu for N restaurants
for x in range(N):
    tempDic = {}
    for y in foodCategories:
        tempFoodArray = {}
        c = 0
        for z in foods:
            if z.category == y:
                if random.random() > 0.4:
                    z.price = random.random()*M+m
                    z.rate = x
                    z.price  = "{:.2f}".format(z.price)
                    tempFoodArray[c] = z.to_dict()
                    c+=1
        tempDic[y] = tempFoodArray
    menu[x] = tempDic

# Dumping the generated menu to json
f = open('menu.json','w')
f.write(json.dumps(menu))
f.close()

# Used if restaurant contains menu. For now they are linked by id.s
##dict_restaurants = {'Restaurants': []}
##restaurants = ['Brookes', 'Fields', 'House of foods', 'Meals time',
##               'Food palace', 'Wonderful meals', 'Hello Foods', 'Dine and Dash',
##               'Pogo Sticks', 'B,B and B\'s', 'Dope or Nope', 'Hell\'s Kitchen']
##
##pizzas = ['New york', 'Margarita', 'BBQ', 'Four Cheese', 'Sun dried Tomato',
##          'California', 'Hawaii']
##burgers = ['Double', 'Single', 'Veg', 'Pork', 'Chicken']
##kebabs = ['Chicken', 'Beef', 'Pork', 'Duck', 'Ostrich']
##soups = ['Tomato', 'Chicken', 'Beef', 'Pork', 'Duck', 'Vegetable']
##drinks = ['Pepsi','Coke', 'Fanta', '7up', 'Thumbs up', 'Wine', 'Beer']
##rice = ['Tomato Rice', 'Vegetable Rice', 'Fried Rice']
##salads = ['Ceasar Salad', 'Cheese Salad', 'French Fries']
##
##foods = {'pizzas':pizzas, 'burgers': burgers, 'kebabs': kebabs,
##         'soups': soups, 'drinks': drinks, 'rice': rice, 'salads': salads}
##for restaurant in restaurants:
##    unique_gen = set()
##    for x in foods.keys():
##        if random.random() > 0.7:
##            num_of_foods = int(random.random()*len(foods[x]))+1
##            for y in range(num_of_foods):
##                p = random.random()*M+m
##                p  = "{:.2f}".format(p)
##                unique_gen.add(Food(random.choice(foods[x]), x, p))
##
##    res = Restaurant(restaurant, list(unique_gen))
##    dict_restaurants['Restaurants'].append(res.to_dict())
