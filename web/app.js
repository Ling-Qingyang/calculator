// ==============================================================================
// Multi-Container Calculator Frontend Application
// Connects to Backend Microservice via Nginx Reverse Proxy (/api/calculate)
// ==============================================================================

(function () {
    'use strict';

    // State Variables
    let currentInput = '0';
    let previousInput = '';
    let pendingOperator = null;
    let shouldResetDisplay = false;
    let isProcessing = false;

    // DOM Elements
    const displayMain = document.getElementById('display-main');
    const displayHistory = document.getElementById('display-history');
    const statusDot = document.getElementById('status-dot');
    const statusTitle = document.getElementById('status-title');
    const historyList = document.getElementById('history-list');
    const btnClearHistory = document.getElementById('btn-clear-history');

    // Initialize
    function init() {
        bindEvents();
        checkBackendHealth();
        setInterval(checkBackendHealth, 8000);
    }

    // Health Check
    async function checkBackendHealth() {
        try {
            const start = performance.now();
            const res = await fetch('/health', { cache: 'no-store' });
            const duration = Math.round(performance.now() - start);

            if (res.ok) {
                const data = await res.json();
                statusDot.className = 'status-dot online';
                statusTitle.textContent = `Backend: Connected (${duration}ms)`;
            } else {
                throw new Error(`HTTP ${res.status}`);
            }
        } catch (err) {
            statusDot.className = 'status-dot error';
            statusTitle.textContent = 'Backend: Offline (Check Compose)';
        }
    }

    // UI Updates
    function updateDisplay() {
        displayMain.textContent = currentInput;
        displayMain.classList.remove('loading', 'error');

        if (pendingOperator !== null && previousInput !== '') {
            displayHistory.textContent = `${previousInput} ${pendingOperator}`;
        } else {
            displayHistory.textContent = '';
        }
    }

    function inputDigit(digit) {
        if (shouldResetDisplay) {
            currentInput = digit === '.' ? '0.' : digit;
            shouldResetDisplay = false;
        } else {
            if (digit === '.') {
                if (!currentInput.includes('.')) {
                    currentInput += '.';
                }
            } else {
                if (currentInput === '0') {
                    currentInput = digit;
                } else {
                    currentInput += digit;
                }
            }
        }
        updateDisplay();
    }

    function setOperator(op) {
        if (pendingOperator !== null && !shouldResetDisplay) {
            performCalculation(false, () => {
                pendingOperator = op;
                previousInput = currentInput;
                shouldResetDisplay = true;
                updateDisplay();
            });
            return;
        }

        previousInput = currentInput;
        pendingOperator = op;
        shouldResetDisplay = true;
        updateDisplay();
    }

    function clearAll() {
        currentInput = '0';
        previousInput = '';
        pendingOperator = null;
        shouldResetDisplay = false;
        isProcessing = false;
        updateDisplay();
        displayHistory.textContent = 'Ready';
    }

    function deleteLastChar() {
        if (shouldResetDisplay) return;
        if (currentInput.length > 1) {
            currentInput = currentInput.slice(0, -1);
        } else {
            currentInput = '0';
        }
        updateDisplay();
    }

    function toggleSign() {
        if (currentInput === '0') return;
        if (currentInput.startsWith('-')) {
            currentInput = currentInput.slice(1);
        } else {
            currentInput = '-' + currentInput;
        }
        updateDisplay();
    }

    // API Call to Backend Container
    async function performCalculation(isFinal = true, callback = null) {
        if (pendingOperator === null || isProcessing) return;

        const a = previousInput;
        const op = pendingOperator;
        const b = currentInput;

        isProcessing = true;
        displayMain.classList.add('loading');
        displayHistory.textContent = `${a} ${op} ${b} =`;

        const startTime = performance.now();

        try {
            // Send request to Nginx reverse proxy endpoint
            const url = `/api/calculate?a=${encodeURIComponent(a)}&op=${encodeURIComponent(op)}&b=${encodeURIComponent(b)}`;
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                }
            });

            const latency = Math.round(performance.now() - startTime);
            const data = await response.json();

            if (!response.ok || data.status === 'error') {
                throw new Error(data.message || `Calculation error (${response.status})`);
            }

            // Success
            let resultVal = data.result;
            // Clean integer output representation
            if (Number.isInteger(resultVal)) {
                resultVal = resultVal.toString();
            } else {
                resultVal = parseFloat(resultVal.toFixed(8)).toString();
            }

            currentInput = resultVal;
            displayMain.textContent = currentInput;
            displayMain.classList.remove('loading', 'error');

            addHistoryItem(a, op, b, resultVal, latency);

            if (isFinal) {
                pendingOperator = null;
                previousInput = '';
                shouldResetDisplay = true;
            }

            if (callback) callback();
        } catch (err) {
            displayMain.classList.remove('loading');
            displayMain.classList.add('error');
            displayMain.textContent = err.message;
            displayHistory.textContent = `Error: ${a} ${op} ${b}`;
            shouldResetDisplay = true;
        } finally {
            isProcessing = false;
        }
    }

    // History List Management
    function addHistoryItem(a, op, b, result, latency) {
        const emptyState = historyList.querySelector('.empty-state');
        if (emptyState) {
            emptyState.remove();
        }

        const item = document.createElement('div');
        item.className = 'history-item';
        item.innerHTML = `
            <span class="history-expr">${escapeHtml(a)} ${escapeHtml(op)} ${escapeHtml(b)} =</span>
            <div>
                <span class="history-res">${escapeHtml(result)}</span>
                <span class="history-meta">${latency}ms</span>
            </div>
        `;

        historyList.prepend(item);

        // Keep maximum 15 items in history
        while (historyList.children.length > 15) {
            historyList.removeChild(historyList.lastChild);
        }
    }

    function escapeHtml(str) {
        return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }

    // Event Listeners
    function bindEvents() {
        // Keypad buttons
        document.querySelectorAll('.btn-num').forEach(btn => {
            btn.addEventListener('click', () => inputDigit(btn.getAttribute('data-num')));
        });

        document.querySelectorAll('.btn-op').forEach(btn => {
            btn.addEventListener('click', () => setOperator(btn.getAttribute('data-op')));
        });

        document.getElementById('btn-clear').addEventListener('click', clearAll);
        document.getElementById('btn-delete').addEventListener('click', deleteLastChar);

        const btnNegate = document.querySelector('[data-action="negate"]');
        if (btnNegate) {
            btnNegate.addEventListener('click', toggleSign);
        }

        document.getElementById('btn-equals').addEventListener('click', () => performCalculation(true));

        btnClearHistory.addEventListener('click', () => {
            historyList.innerHTML = '<div class="empty-state">No calculations executed yet</div>';
        });

        // Keyboard support
        window.addEventListener('keydown', (e) => {
            if ((e.key >= '0' && e.key <= '9') || e.key === '.') {
                inputDigit(e.key);
            } else if (['+', '-', '*', '/'].includes(e.key)) {
                setOperator(e.key);
            } else if (e.key === 'Enter' || e.key === '=') {
                e.preventDefault();
                performCalculation(true);
            } else if (e.key === 'Backspace') {
                deleteLastChar();
            } else if (e.key === 'Escape') {
                clearAll();
            }
        });
    }

    // Launch
    document.addEventListener('DOMContentLoaded', init);
})();
