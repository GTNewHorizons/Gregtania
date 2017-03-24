package com.gmail.pharaun.gregtania.botania;

import com.gmail.pharaun.gregtania.Reference;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;

import java.util.Iterator;

// Thanks Magical Beees and ExCompressium for various hints on how to disable a flower/add a new flower
public class Util {
    public static void registerFlower(String name, Class klass) {
        BotaniaAPI.registerSubTile(name, klass);
        BotaniaAPI.registerSubTileSignature(klass, new CustomSubTileSignature(name));
        BotaniaAPI.addSubTileToCreativeMenu(name);
    }

    public static void registerFunctionalPetalRecipe(String name, Object... petals) {
        registerFunctionalPetalRecipeTier(name, false, petals);
    }

    public static void registerFunctionalPetalRecipeElven(String name, Object... petals) {
       registerFunctionalPetalRecipeTier(name, true, petals);
    }

    private static void registerFunctionalPetalRecipeTier(String name, boolean elven, Object... petals) {
        ItemStack flower = ItemBlockSpecialFlower.ofType(name);
        RecipePetals recipeFlowerEvolved = BotaniaAPI.registerPetalRecipe(flower, petals);

        String lexiconLangPage = "tile.botania:flower." + Reference.MODID + "." + name + ".page";
        LexiconPage lexiconRecipe = new PagePetalRecipe(lexiconLangPage + ".1", recipeFlowerEvolved);

        registerLexicon(name, elven, new PageText(lexiconLangPage + ".0"), lexiconRecipe);
    }

    public static void registerFunctionalRunicRecipe(String name, int mana, Object... petals) {
        registerFunctionalRunicRecipe(name, false, mana, petals);
    }

    public static void registerFunctionalRunicRecipeElven(String name, int mana, Object... petals) {
        registerFunctionalRunicRecipe(name,true, mana, petals);
    }

    private static void registerFunctionalRunicRecipe(String name, boolean elven, int mana, Object... petals) {
        ItemStack flower = ItemBlockSpecialFlower.ofType(name);
        RecipeRuneAltar recipeFlowerEvolved = BotaniaAPI.registerRuneAltarRecipe(flower, mana, petals);

        String lexiconLangPage = "tile.botania:flower." + Reference.MODID + "." + name + ".page";
        PageRuneRecipe lexiconRecipe = new PageRuneRecipe(lexiconLangPage + ".1", recipeFlowerEvolved);

        registerLexicon(name, elven, new PageText(lexiconLangPage + ".0"), lexiconRecipe);
    }

    private static void registerLexicon(String name, boolean elven, LexiconPage... pages) {
        String lexiconLangName = "tile.botania:flower." + Reference.MODID + "." + name + ".name";
        LexiconEntry lexicon = new LexiconEntry(lexiconLangName, BotaniaAPI.categoryFunctionalFlowers) {
            @Override
            public String getTagline() {
                // TODO: is this the right one or is there another one?
                return "tile.botania:flower." + Reference.MODID + "." + name + ".reference";
            }
        };
        lexicon.setLexiconPages(pages);
        lexicon.setPriority();
        if (elven) {
            lexicon.setKnowledgeType(BotaniaAPI.elvenKnowledge);
        }

        BotaniaAPI.addEntry(lexicon, lexicon.category);
    }

    /*
     * TODO: doesn't remove the flower from nei or creative panel, still need to mark em as disabled with minetweaker
     */
    public static void disableBotaniaFunctionalFlower(String name) {
        Iterator<LexiconEntry> it = BotaniaAPI.getAllEntries().iterator();
        while(it.hasNext()) {
            if(it.next().getUnlocalizedName().equals("botania.entry." + name)) {
                it.remove();
                break;
            }
        }
        it = BotaniaAPI.categoryFunctionalFlowers.entries.iterator();
        while(it.hasNext()) {
            if(it.next().getUnlocalizedName().equals("botania.entry." + name)) {
                it.remove();
                break;
            }
        }
        Iterator<RecipePetals> it2 = BotaniaAPI.petalRecipes.iterator();
        ItemStack itemStack = ItemBlockSpecialFlower.ofType(name);
        while(it2.hasNext()) {
            ItemStack output = it2.next().getOutput();
            if(ItemStack.areItemStacksEqual(itemStack, output)) {
                it2.remove();
                break;
            }
        }
    }
}
