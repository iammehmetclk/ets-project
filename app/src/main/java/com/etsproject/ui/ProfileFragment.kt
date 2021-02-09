package com.etsproject.ui

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.etsproject.R
import com.etsproject.data.*
import com.etsproject.databinding.FragmentProfileBinding
import com.etsproject.viewmodel.MainViewModel
import com.etsproject.viewmodel.MainViewModelFactory

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            ProfileRepository.getInstance(
                ApplicationDatabase
                    .getApplicationDatabase(requireContext()).profileDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var profile: Profile? = null
        arguments?.getSerializable("profile")?.let { profile = it as Profile }
        val binding: FragmentProfileBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_profile, container, false)

        var pk: Long = System.currentTimeMillis()
        var name: String? = null
        var surname: String? = null
        var birthDate: BirthDate? = null
        var mailAddress: String? = null
        var phoneNumber: PhoneNumber? = null
        var note: String? = null

        binding.apply {
            profile?.let { theprofile ->
                pk = theprofile.pk
                headerText.text = getString(R.string.kisi_guncelle)
                nameText.setText(theprofile.name)
                surnameText.setText(theprofile.surname)
                birthDate = theprofile.birthDate
                birthDateText.setText(theprofile.birthDate.toString())
                datePicker.updateDate(birthDate!!.year, birthDate!!.month, birthDate!!.day)
                epostaText.setText(theprofile.mailAddress)
                spinner.setSelection(getCountryCodePosition(theprofile.phoneNumber?.countryCode))
                phoneNumberText.setText(theprofile.phoneNumber?.number)
                theprofile.note?.let { noteText.setText(it) }
            }

            backButton.setOnClickListener {
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                requireActivity().onBackPressed()
            }

            birthDateText.setOnClickListener {
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                datePickerLayout.visibility = View.VISIBLE
            }

            datePickerButton.setOnClickListener {
                val date = "${datePicker.dayOfMonth}/${datePicker.month}/${datePicker.year}"
                birthDate = BirthDate(datePicker.dayOfMonth, datePicker.month, datePicker.year)
                birthDateText.setText(date)
                datePickerLayout.visibility = View.GONE
            }

            savedLayout.setOnClickListener {
                savedLayout.visibility = View.GONE
            }

            phoneNumberText.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 4 && s[3].toString() != " ") s.insert(3, " ")
                    if (s?.length == 8 && s[7].toString() != " ") s.insert(7, " ")
                    if (s?.length == 11 && s[10].toString() != " ") s.insert(10, " ")
                }
            })

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position == 0) phoneNumberText.filters =
                        arrayOf(InputFilter.LengthFilter(13))
                    else phoneNumberText.filters = arrayOf(InputFilter.LengthFilter(20))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            saveButton.setOnClickListener {

                if (nameText.text.isNullOrBlank()) {
                    nameTextLayout.error = "Bu alan boş bırakılamaz"
                    name = null
                } else {
                    nameText.text?.let {
                        when {
                            it.trim().length < 2 -> {
                                nameTextLayout.error = "En az 2 karakter olmalı."
                                name = null
                            }
                            else -> {
                                nameTextLayout.error = null
                                name = it.trim().toString()

                            }
                        }
                    }
                }

                if (surnameText.text.isNullOrBlank()) {
                    surnameTextLayout.error = "Bu alan boş bırakılamaz"
                    surname = null
                } else {
                    surnameText.text?.let {
                        when {
                            it.trim().length < 2 -> {
                                surnameTextLayout.error = "En az 2 karakter olmalı."
                                surname = null
                            }
                            else -> {
                                surnameTextLayout.error = null
                                surname = it.trim().toString()
                            }
                        }
                    }
                }

                if (birthDateText.text.isNullOrBlank()) {
                    birthDateTextLayout.error = "Bu alan boş bırakılamaz"
                } else birthDateTextLayout.error = null

                if (epostaText.text.isNullOrBlank()) {
                    epostaTextLayout.error = "Bu alan boş bırakılamaz"
                    mailAddress = null
                } else {
                    epostaText.text?.let {
                        when {
                            it.trim().length > 60 -> {
                                epostaTextLayout.error = "En fazla 60 karakter olmalı."
                                mailAddress = null
                            }
                            android.util.Patterns.EMAIL_ADDRESS.matcher(it.trim()).matches() -> {
                                epostaTextLayout.error = null
                                mailAddress = it.trim().toString()
                            }
                            else -> {
                                epostaTextLayout.error = "Geçersiz mail adresi."
                                mailAddress = null
                            }
                        }
                    }
                }

                if (phoneNumberText.text.isNullOrBlank()) {
                    phoneNumberTextLayout.error = "Bu alan boş bırakılamaz"
                    phoneNumber = null
                } else {
                    if (spinner.selectedItem.equals("+90")) {
                        phoneNumberText.text?.let {
                            if (it.trim().length > 13) {
                                phoneNumberTextLayout.error = "En fazla 10 rakam olmalı."
                                phoneNumber = null
                            } else {
                                phoneNumberTextLayout.error = null
                                phoneNumber = PhoneNumber(
                                    spinner.selectedItem.toString(),
                                    it.trim().toString()
                                )
                            }
                        }
                    } else phoneNumberText.text?.let {
                        phoneNumberTextLayout.error = null
                        phoneNumber =
                            PhoneNumber(spinner.selectedItem.toString(), it.trim().toString())
                    }
                }

                if (noteText.text.isNullOrBlank()) note = null
                else {
                    noteText.text?.let {
                        if (it.trim().length > 100) {
                            noteTextLayout.error = "En fazla 100 karakter olmalı."
                            note = null
                        } else {
                            noteTextLayout.error = null
                            note = it.trim().toString()
                        }
                    }
                }

                if (name != null && surname != null && birthDate != null
                    && mailAddress != null && phoneNumber != null
                ) {
                    if (profile != null) {
                        viewModel.updateProfile(
                            Profile(
                                pk,
                                name,
                                surname,
                                birthDate,
                                mailAddress,
                                phoneNumber,
                                note
                            )
                        )
                    } else {
                        val newProfile = Profile(pk, name, surname, birthDate, mailAddress, phoneNumber, note)
                        profile = newProfile
                        viewModel.insertProfile(newProfile)
                    }
                    headerText.text = getString(R.string.kisi_guncelle)
                    savedLayout.visibility = View.VISIBLE
                } else scrollView.fullScroll(View.FOCUS_UP)
            }
        }
        return binding.root
    }

    private fun getCountryCodePosition(code: String?): Int {
        code?.let {
            val array = resources.getStringArray(R.array.country_code).toList()
            array.forEachIndexed { index, s ->
                if (s == code) return index
            }
        }
        return 0
    }
}