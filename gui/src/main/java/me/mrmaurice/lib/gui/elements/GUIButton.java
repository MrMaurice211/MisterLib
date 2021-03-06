package me.mrmaurice.lib.gui.elements;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;

import lombok.ToString;
import me.mrmaurice.lib.gui.MenuGUI;
import me.mrmaurice.lib.gui.enums.Icon;
import me.mrmaurice.lib.gui.events.GUIEvent;
import me.mrmaurice.lib.reflections.Reflections;
import me.mrmaurice.lib.reflections.Version;
import me.mrmaurice.lib.utils.Util;

@ToString
@SuppressWarnings("deprecation")
public class GUIButton {

    private static ItemFactory factory = Bukkit.getItemFactory();

    private final List<MetaApplier> pendant = Lists.newCopyOnWriteArrayList();
    private Consumer<GUIEvent> action;
    private Material type = Material.BEDROCK;
    private GameProfile profile;
    private int amount = 1;
    private ItemMeta im;
    private byte data;

//	public GUIButton(String mat) {
//		type = mat.toUpperCase();
//	}

    public GUIButton(Material mat) {
        withType(mat);
    }

    public GUIButton(ItemStack item) {
        withItem(item);
    }

    public GUIButton withItem(ItemStack item) {
        im = item.getItemMeta();
        withType(item.getType());
        withAmount(item.getAmount());
        withData(item.getDurability());
        return this;
    }

    public GUIButton withType(Material newType) {
        Validate.notNull(newType);
        type = newType;
        return this;
    }

    public GUIButton withName(String newName) {
        Validate.notNull(newName);
        applyMeta(ItemMeta.class, m -> m.setDisplayName(Util.color(newName)));
        return this;
    }

    public GUIButton withAmount(int newAmount) {
        amount = newAmount;
        return this;
    }

    public GUIButton withData(int newData) {
        data = (byte) newData;
        return this;
    }

    public GUIButton addFlag(ItemFlag... flags) {
        Validate.notNull(flags);
        applyMeta(ItemMeta.class, m -> m.addItemFlags(flags));
        return this;
    }

    public GUIButton withLore(String... strings) {
        return withLore(Arrays.asList(strings));
    }

    public GUIButton withLore(List<String> newLore) {
        Validate.isTrue(newLore != null && !newLore.isEmpty());
        applyMeta(ItemMeta.class, m -> m.setLore(Util.colorList(newLore)));
        return this;
    }

    public GUIButton addLore(String... toAdd) {
        return addLore(Arrays.asList(toAdd));
    }

    public GUIButton addLore(List<String> toAdd) {
        toAdd.forEach(this::addLore);
        return this;
    }

    public GUIButton addLore(String newLine) {
        Validate.notNull(newLine);
        String line = Util.color(newLine);
        applyMeta(m -> m.setLore(Util.add(getFromMeta(ItemMeta::getLore), line)));
        return this;
    }

    public GUIButton addEnchantment(Enchantment ench, int level) {
        return addEnchantment(ench, level, true);
    }

    public GUIButton addEnchantment(Enchantment toAdd, int level, boolean unsafe) {
        applyMeta(m -> m.addEnchant(toAdd, level, unsafe));
        return this;
    }

    public GUIButton removeEnchantment(Enchantment ench) {
        applyMeta(m -> m.removeEnchant(ench));
        return this;
    }

    public GUIButton clearEnchantments() {
        if (!getFromMeta(ItemMeta::hasEnchants)) {
            return this;
        }
        for (Enchantment ench : getFromMeta(ItemMeta.class, ItemMeta::getEnchants).keySet()) {
            removeEnchantment(ench);
        }
        return this;
    }

    public GUIButton addPotionEffect(PotionEffect effect) {
        applyMeta(PotionMeta.class, m -> m.addCustomEffect(effect, true));
        return this;
    }

    public GUIButton clearPotionEffects() {
        applyMeta(PotionMeta.class, PotionMeta::clearCustomEffects);
        return this;
    }

    public GUIButton withOwner(String newOwner) {
        return applyMeta(SkullMeta.class, m -> m.setOwner(newOwner));
    }

    public GUIButton withProfile(Icon head) {
        return withProfile(head.getProfile());
    }

    public GUIButton withProfile(GameProfile newProfile) {
        Validate.notNull(newProfile);
        profile = newProfile;
        return this;
    }

    public GUIButton withColor(Color newColor) {
        return applyMeta(LeatherArmorMeta.class, m -> m.setColor(newColor));
    }

    public GUIButton withEffect(PotionEffect toAdd, boolean bool) {
        return applyMeta(PotionMeta.class, m -> m.addCustomEffect(toAdd, bool));
    }

    public GUIButton onClick(Consumer<GUIEvent> cons) {
        action = cons;
        return this;
    }

    public void run(InventoryClickEvent event) {
        if (action != null) {
            action.accept(
                    new GUIEvent((Player) event.getWhoClicked(), (MenuGUI) event.getInventory().getHolder()));
        }
    }

    public ItemStack toItemStack() {
        ItemStack item;

        if (!is13()) {
            item = new ItemStack(type, amount, (short) data);
        } else {
            item = new ItemStack(type, amount);
            if (type.getMaxDurability() > 0) {
                item.setDurability((short) (type.getMaxDurability() - data));
            }
        }

        if (im == null) {
            im = factory.getItemMeta(type);
            pendant.forEach(MetaApplier::apply);
        }

        item.setItemMeta(im);

        if (im instanceof SkullMeta && profile != null) {
            SkullMeta sm = (SkullMeta) item.getItemMeta();
            setProfile(item, sm);
        }
        return item;

    }

    @SuppressWarnings("unchecked")
    public <T extends ItemMeta, R> R getFromMeta(Function<T, R> metaRetriever) {
        Class<T> metaType = (Class<T>) ItemMeta.class;
        return getFromMeta(metaType, metaRetriever);
    }

    public <T extends ItemMeta, R> R getFromMeta(Class<T> metaType, Function<T, R> metaRetriever) {
        if (!metaType.isAssignableFrom(im.getClass())) {
            return null;
        }

        T specificMeta = metaType.cast(im);
        return metaRetriever.apply(specificMeta);
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemMeta> GUIButton applyMeta(Consumer<T> metaApplier) {
        Class<T> metaType = (Class<T>) ItemMeta.class;
        return applyMeta(metaType, metaApplier);
    }

    public <T extends ItemMeta> GUIButton applyMeta(Class<T> metaType, Consumer<T> metaApplier) {

        if (im == null) {
            pendant.add(() -> applyMeta(metaType, metaApplier));
            return this;
        }

        if (!metaType.isAssignableFrom(im.getClass())) {
            return this;
        }

        T specificMeta = metaType.cast(im);
        metaApplier.accept(specificMeta);
        return this;
    }

    private interface MetaApplier {

        void apply();
    }

    private void setProfile(ItemStack item, SkullMeta skullMeta) {
        Field profileField = null;

        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            Util.exception(e);
        }

        if (profileField != null) {
            profileField.setAccessible(true);
        }

        try {
            if (profileField != null) {
                profileField.set(skullMeta, profile);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            Util.exception(e);
        }

        item.setItemMeta(skullMeta);
    }

    private boolean is13() {
        return Reflections.getVersion().isAbove(Version.v12);
    }

}
