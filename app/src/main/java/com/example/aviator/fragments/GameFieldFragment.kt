package com.example.aviator.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.*
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aviator.R
import com.example.aviator.SharedPreferences
import com.example.aviator.databinding.FragmentGameFieldBinding
import com.example.aviator.navigator
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class GameFieldFragment : Fragment(R.layout.fragment_game_field) {

    companion object {
        const val TAG = "Game"
        private const val VIBRATOR = "Vibrator"
        private const val MUSIC = "Music"
        fun newInstance(musicStatus: Boolean, vibratorStatus: Boolean) = GameFieldFragment().apply {
            arguments = bundleOf(MUSIC to musicStatus, VIBRATOR to vibratorStatus)
        }
    }

    private val binding by viewBinding(FragmentGameFieldBinding::bind)
    private val scope by lazy { CoroutineScope(Dispatchers.Main) }
    private val animRocket by lazy {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.anim_rocket
        ) as AnimationDrawable
    }

    private var isClaimed = false
    private var balance = 0
    private var display = DisplayMetrics()
    private var isAlive = true
    private var bet = 100
    private var rocketX = 0f
    private var rocketY = 0f
    private var player: MediaPlayer? = null

    override fun onAttach(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            balance = SharedPreferences(context).getBalance()
        }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().windowManager.defaultDisplay.getMetrics(display)
        setUpBalance()
        setUpClickListeners()
        setUpControlPanel()
        balance -= 100
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopChronometer()
        stopRocketSound()
        scope.cancel()
    }

    override fun onDetach() {
        super.onDetach()
        SharedPreferences(requireContext()).saveBalance(balance)
    }

    private fun setUpControlPanel() = with(binding) {
        betControl.typeTv.text = getString(R.string.betTv)
        setUpBet()
    }

    private fun setUpBet() {
        binding.betControl.valueTv.text = bet.toString()
    }

    private fun stopFlight() {
        isClaimed = true
    }

    private fun checkRate(claim: String): String {
        return if (claim.length == 3) {
            claim
        } else {
            claim.substring(0, 3)
        }
    }

    private fun startChronometer() {
        binding.chronometer.base = SystemClock.elapsedRealtime()
        binding.chronometer.start()
    }

    private fun stopChronometer() {
        binding.chronometer.stop()
    }

    private fun moveRocket() = with(binding) {
        rocket.background = animRocket
        rocket.setImageResource(0)
        animRocket.start()

        scope.launch {
            while (isAlive) {
                delay(50)
                if (rocket.y > calculateDestination(display.heightPixels.toFloat()))
                    rocket.y -= 5f
                else break
            }
        }
    }

    private fun calculateDestination(heightOfScree: Float): Float {
        return (heightOfScree * 10) / 100
    }

    private fun setUpBalance() {
        binding.balanceTv.text = balance.toString()
    }

    private fun setUpClickListeners() = with(binding) {
        startBtn.setOnClickListener {
            rocketX = rocket.x
            rocketY = rocket.y
            if (bet >= 100) {

                controlPanelGroup.isVisible = false
                claimBtn.isVisible = true
                moveRocket()
                startChronometer()
                startCountDown()
                startRate()
                playRocketSound()
            } else {
                navigator().showToast(getString(R.string.minBet))
            }
        }
        betControl.minusImg.setOnClickListener {
            if (bet > 200) {
                bet -= 100
                balance += 100
                setUpBalance()
                setUpBet()
            }
        }
        betControl.pusImg.setOnClickListener {
            if (balance >= 100)
                bet += 100
            balance -= 100
            setUpBalance()
            setUpBet()
        }

        tryAgainBtn.setOnClickListener {
            controlPanelGroup.isVisible = true
            groupResult.isVisible = false
            isClaimed = false
            isAlive = true
            bet = 100
            balance -= 100
            setUpBalance()
            binding.rateTv.isVisible = false
            setUpRocketsDefaultParams()
            setUpBet()

        }
        backToMenuBtn.setOnClickListener {
            navigator().back()
        }
        claimBtn.setOnClickListener {
            stopFlight()
        }
    }

    private fun setUpRocketsDefaultParams() = with(binding) {
        rocket.y = rocketY
        rocket.x = rocketX
        rocket.setImageResource(R.drawable.rocket1)
    }

    private fun startCountDown() {
        scope.launch {
            var randomTime = getRandom() * 1000
            while (randomTime > 0) {
                if (isClaimed) {
                    break
                }
                delay(1000)
                randomTime -= 1000
            }
            if (!isClaimed) {
                blowUpTheRocket()
            }
        }
    }
   private fun getRandom(): Int {
        return when ((1..10).random()) {
            in 1..5 -> (1..2).random()
            in 6..7 -> 3
            8 -> (4..5).random()
            9 -> (6..7).random()
            else  -> (8..10).random()
        }
    }

    private fun stopRocket() {
        isAlive = false
        stopChronometer()
        stopRocketSound()
        animRocket.stop()
        binding.rocket.background = null
        binding.rocket.setImageResource(R.drawable.rocket6)
    }

    private fun blowUpTheRocket() = with(binding) {
        isAlive = false
        stopChronometer()
        stopRocketSound()
        animRocket.stop()
        rocket.background = null
        rocket.setImageResource(R.drawable.explosion_min)
        playExplosionSound()
        startVibration()
    }

    @SuppressLint("SetTextI18n")
    private fun startRate() {
        var currentRate = 1.0
        binding.rateTv.isVisible = true
        scope.launch {
            while (isAlive) {
                binding.rateTv.text = "${checkRate(currentRate.toString())}X"
                if (isClaimed) {
                    balance += (bet * currentRate).roundToInt()
                    binding.resultTv.text = getString(R.string.resultPositive)
                    binding.groupResult.isVisible = true
                    binding.claimBtn.isVisible = false
                    setUpBalance()
                    stopRocket()
                    break
                }
                delay(150)
                currentRate += 0.1
            }

            if (!isClaimed) {
                binding.resultTv.text = getString(R.string.resultNegative)
                binding.groupResult.isVisible = true
                binding.claimBtn.isVisible = false
                setUpBalance()
            }
        }
    }

    private fun playRocketSound() {
        if (checkMusicStatus()) {
            player = MediaPlayer.create(requireContext(), R.raw.rocket)
            player?.start()
        }
    }

    private fun stopRocketSound() {
        if (checkMusicStatus()) {
            player?.stop()
        }
    }

    private fun playExplosionSound() {
        if (checkMusicStatus()) {
            val player = MediaPlayer.create(requireContext(), R.raw.explosion)
            player.start()
        }
    }

    private fun checkMusicStatus(): Boolean {
        return requireArguments().getBoolean(MUSIC)
    }

    private fun startVibration() {
        if(requireArguments().getBoolean(VIBRATOR)) {
            val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            val milliseconds = 1000L
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            milliseconds,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(milliseconds)
                }
            }
        }
    }
}