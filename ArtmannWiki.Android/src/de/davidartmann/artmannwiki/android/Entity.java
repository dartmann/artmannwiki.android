package de.davidartmann.artmannwiki.android;

/**
 * Created by David on 29.08.2014.
 */
public class Entity {
    private int _id;
    private String _benutzer, _password, _email, _device, _number, _pin, _puk, _kategorie, _info;

    public Entity() {    }

    //Kontruktor für E-Mail:
    public Entity(String email, String password) {
        this._email = email;
        this._password = password;
    }

    //Kontruktor für Login:
    public Entity(String benutzer, String password, String info) {
        this._benutzer = benutzer;
        this._password = password;
        this._info = info;
    }

    //Kontruktor für neues Handy/Tablet:
    public Entity(String device, String number, String pin, String puk) {
        this._device = device;
        this._number = number;
        this._pin = pin;
        this._puk = puk;
    }

    public Entity(String benutzer, String password, String email, String device,
                  String number, String pin, String puk, String kategorie, String info) {
        this._benutzer = benutzer;
        this._password = password;
        this._email = email;
        this._device = device;
        this._number = number;
        this._pin = pin;
        this._puk = puk;
        this._kategorie = kategorie;
        this._info = info;
    }

    public Entity(int id, String benutzer, String password, String email, String device,
                  String number, String pin, String puk, String kategorie, String info) {
        this._id = id;
        this._benutzer = benutzer;
        this._password = password;
        this._email = email;
        this._device = device;
        this._number = number;
        this._pin = pin;
        this._puk = puk;
        this._kategorie = kategorie;
        this._info = info;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_benutzer() {
        return _benutzer;
    }

    public void set_benutzer(String _benutzer) {
        this._benutzer = _benutzer;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_device() {
        return _device;
    }

    public void set_device(String _device) {
        this._device = _device;
    }

    public String get_number() {
        return _number;
    }

    public void set_number(String _number) {
        this._number = _number;
    }

    public String get_pin() {
        return _pin;
    }

    public void set_pin(String _pin) {
        this._pin = _pin;
    }

    public String get_puk() {
        return _puk;
    }

    public void set_puk(String _puk) {
        this._puk = _puk;
    }

    public String get_kategorie() {
        return _kategorie;
    }

    public void set_kategorie(String _kategorie) {
        this._kategorie = _kategorie;
    }

    public String get_info() {
        return _info;
    }

    public void set_info(String _info) {
        this._info = _info;
    }
}
